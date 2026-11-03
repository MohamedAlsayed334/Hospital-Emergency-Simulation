public class Patient implements Runnable {
    private final int id;
    private final WaitingRoom waitingRoom;

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    public String getLabel() {
        return "P" + id;
    }

    private void enterWaitingRoom() {
        try {
            if (!waitingRoom.tryPut(this)) {
                Logger.log("The waiting room is full.",
                        getLabel() + " is waiting outside for an available seat.");
                waitingRoom.put(this); // blocks until a seat is free

            }
            // "entered the waiting room" is logged inside WaitingRoom.insert()
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Called by a doctor whose treatment-room attempt timed out.
    // Runs in a NEW thread so the doctor returns to idle immediately.
    public void returnToWaitingRoom() {
        new Thread(this::enterWaitingRoom).start();
    }

    @Override
    public void run() {
        Logger.log(getLabel() + " has arrived at the hospital.");
        enterWaitingRoom();
    }
}
