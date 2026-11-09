import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Doctor implements Runnable {
    private final int id;
    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;
    private final AtomicInteger treatedCount;

    private final Random random = new Random();

    public Doctor(int id, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms,
                  AtomicInteger treatedCount) {
        this.id = id;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.treatedCount = treatedCount;
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

                    // print BEFORE releasing, so no other doctor can log "acquired" first
                    Logger.log("Doctor " + id + " finished treating " + label + ".",
                            "Doctor " + id + " released the treatment room.");
                } finally {
                    treatmentRooms.release();    // no logging here anymore
                }
                Logger.log("Doctor " + id + " is now available.",
                        label + " left.");
                treatedCount.incrementAndGet();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}