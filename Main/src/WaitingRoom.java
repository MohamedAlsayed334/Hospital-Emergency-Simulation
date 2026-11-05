public class WaitingRoom {

    private int CAPACITY;
    private final Patient[] buffer;
    private int in = 0, out = 0;

    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutualExclusion;

    public WaitingRoom(int capacity) {

        this.CAPACITY = capacity;
        buffer = new Patient[capacity];
        empty = new Semaphore(capacity);
        full = new Semaphore(0);
        mutualExclusion = new Semaphore(1);
    }

    public void insert(Patient patient) throws InterruptedException {
        mutualExclusion.acquire();
        buffer[in] = patient;
        in = (in + 1) % CAPACITY;
        Logger.log(patient.getLabel() + " has entered the waiting room.");
        mutualExclusion.release();
        full.release();
    }

    // Non-blocking: returns false if there is no free seat
    public boolean tryPut(Patient patient) throws InterruptedException {
        if (!empty.tryAcquire()) {
            return false;
        }
        insert(patient);
        return true;
    }

    public void put(Patient p) throws InterruptedException {
        empty.acquire();
        insert(p);
    }

    // Blocking: the doctor stays idle until a patient is available
    public Patient take(int doctorId) throws InterruptedException {
        full.acquire();
        mutualExclusion.acquire();
        Patient p = buffer[out];
        buffer[out] = null;
        out = (out + 1) % CAPACITY;
        Logger.log("Doctor " + doctorId + " is assigned to " + p.getLabel() + ".");
        mutualExclusion.release();
        empty.release();
        return p;
    }
}