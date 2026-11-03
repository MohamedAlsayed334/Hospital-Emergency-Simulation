public class Semaphore {
    protected int value;
    public Semaphore() { value = 0; }
    public Semaphore(int initial) { value = initial; }

    public synchronized void P() {
        try {
            while (value == 0) {
                wait();
            }
            value--;
        } catch (InterruptedException e) {
            notify();
            Thread.currentThread().interrupt();
        }
    }
    public synchronized void V() {
        value++;
        notify();
    }
    public synchronized boolean tryAcquire(long timeout) {
        long deadline = System.currentTimeMillis() + timeout;
        try {
            while (value == 0) {
                long remaining = deadline - System.currentTimeMillis();
                if (remaining <= 0) {
                    return false;
                }
                wait(remaining);
            }
            value--;
            return true;
        } catch (InterruptedException e) {
            notify();
            Thread.currentThread().interrupt();
            return false;
        }
    }
}