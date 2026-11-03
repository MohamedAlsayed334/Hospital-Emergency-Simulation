import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Doctor implements Runnable {
    private final int id;
    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;
    private final AtomicInteger treatedCount;
    private final int totalPatients;
    private final Random random = new Random();

    public Doctor(int id, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms,
                  AtomicInteger treatedCount, int totalPatients) {
        this.id = id;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.treatedCount = treatedCount;
        this.totalPatients = totalPatients;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Patient patient = waitingRoom.take();
                Hospital.log("Doctor " + id + " assigned to " + patient);

                if (!treatmentRooms.tryAcquire()) {
                    Hospital.log("Doctor " + id + " could not acquire a treatment room");
                    Hospital.log("Doctor " + id + " is sending " + patient + " back");
                    new Thread(() -> waitingRoom.enter(patient)).start();
                    continue;
                }

                try {
                    Hospital.log("Doctor " + id + " acquired a treatment room");
                    Hospital.log("Doctor " + id + " begins treating " + patient);
                    Thread.sleep(2000 + random.nextInt(3001));
                    Hospital.log("Doctor " + id + " finished treating " + patient);
                } finally {
                    treatmentRooms.release();
                    Hospital.log("Treatment room released by Doctor " + id);
                }

                Hospital.log(patient + " left the treatment room");
                if (treatedCount.incrementAndGet() == totalPatients) {
                    Hospital.finish();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
