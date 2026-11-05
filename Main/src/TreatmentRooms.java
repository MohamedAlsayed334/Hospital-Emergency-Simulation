public class TreatmentRooms {
    private static final int NUM_ROOMS = 2;
    private static final long TIMEOUT_MS = 3000;

    private final Semaphore semaphore = new Semaphore(NUM_ROOMS);
    public boolean tryAcquire() {
        return semaphore.tryAcquire(TIMEOUT_MS);
    }
    public void release() {
        semaphore.release();
    }
}