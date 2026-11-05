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
                Patient patient = waitingRoom.take(id);
                String label = patient.getLabel();

                Logger.log("Doctor " + id + " is trying to acquire a treatment room.");
                if (!treatmentRooms.tryAcquire()) {
                    Logger.log("Doctor " + id + " could not acquire a treatment room.",
                            "Doctor " + id + " is sending " + label + " back to the waiting room.");
                    patient.returnToWaitingRoom();
                    continue;
                }
                try {
                    Logger.log("Doctor " + id + " acquired a treatment room.",
                            "Doctor " + id + " begins treating " + label + ".");
                    Thread.sleep(2000 + random.nextInt(3001));
                    Logger.log("Doctor " + id + " finished treating " + label + ".");
                } finally {
                    treatmentRooms.release();
                    Logger.log("Doctor " + id + " released the treatment room.");
                }
                Logger.log("Doctor " + id + " is now available.",
                        label + " left.");
                if (treatedCount.incrementAndGet() == totalPatients) {
                    Hospital.finish();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}