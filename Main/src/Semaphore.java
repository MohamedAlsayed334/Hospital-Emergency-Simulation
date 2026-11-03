public class Semaphore {
    protected int value;
    public Semaphore() { value = 0; }
    public Semaphore(int initial) { value = initial; }

    public synchronized void P() { // normal wait
        try {
            while (value == 0) {
                wait();
            }
            value--;
        } catch (InterruptedException e) {
            notify();                       // pass the wakeup on
            Thread.currentThread().interrupt();
        }
    }
    public synchronized void V() { // normal notify the waiting thread
        value++;
        notify();
    }
    // true if acquired, false if timed out (timeout in milliseconds)
    public synchronized boolean tryAcquire(long timeout) {
        long deadline = System.currentTimeMillis() + timeout;
        try {
            while (value == 0) { // no treatment room available
                long remaining = deadline - System.currentTimeMillis();
                if (remaining <= 0) {
                    return false;           // timed out, nothing was taken
                }
                wait(remaining); // else wait (the remaining)
            }
            value--;
            return true;
        } catch (InterruptedException e) {
            notify();
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public synchronized boolean tryAcquire() {
       if(value > 0) {
           value--;
           return true;
       }
       return false;
    }
}