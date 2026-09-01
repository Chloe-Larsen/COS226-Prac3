import java.util.concurrent.atomic.AtomicBoolean;

public class TASLock {

    private final AtomicBoolean locked = new AtomicBoolean(false);

    /* Do not modify this method */
    private boolean testAndSet() {
        return locked.getAndSet(true);
    }

    public void lock() {
        while (testAndSet()) {
            Thread.onSpinWait(); // busy waiting
        }
    }

    public void unlock() {
        locked.set(false);
    }
}