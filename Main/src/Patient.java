public class Patient implements Runnable {
    private final int id;
    private final WaitingRoom waitingRoom;

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    public String getLabel() { return "P" + id; }

    // Non-blocking. "entered the waiting room" is logged inside WaitingRoom.insert().
    private boolean tryEnter() {
        try {
            if (waitingRoom.tryPut(this)) return true;
            // "entered the waiting room" is logged inside WaitingRoom.insert()
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Logger.log("The waiting room is full.",
                getLabel() + " is waiting outside for an available seat.");
        return false;
    }

    // Called by Hospital, one patient at a time, in the caller's thread.
    public void arrive() {
        Logger.log(getLabel() + " arrived.");
        if (!tryEnter()) {
            new Thread(this).start();   // wait outside without blocking Hospital
        }
    }

    // Called by a doctor whose treatment-room attempt timed out.
    // Runs in a NEW thread so the doctor returns to idle immediately.
    public void returnToWaitingRoom() {
        if (!tryEnter()) {
            new Thread(this).start();
        }
    }

    // Body of the "waiting outside" thread: blocks until a seat is free.
    @Override
    public void run() {
        try {
            waitingRoom.put(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}