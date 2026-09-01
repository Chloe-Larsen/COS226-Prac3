import java.util.concurrent.atomic.AtomicBoolean;

public class TASLock implements Lock {

    private final AtomicBoolean locked = new AtomicBoolean(false);

    /* Do not modify this method */
    private boolean testAndSet() {
        return locked.getAndSet(true);
    }

    @Override
    public void lock() {
        while (testAndSet()) {
            Thread.onSpinWait(); // busy waiting
        }
    }

    @Override
    public void unlock() {
        locked.set(false);
    }
}