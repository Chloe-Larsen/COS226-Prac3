import java.util.concurrent.atomic.AtomicBoolean;

public class TASLock extends Lock {

    private final AtomicBoolean locked = new AtomicBoolean(false);

    /* Do not modify this method */
    private boolean testAndSet() {
        return locked.getAndSet(true);
    }

    private boolean doTestAndSet() {
        incrementTestAndSetCount();
        return testAndSet();
    }

    @Override
    public void lock() {
        while (doTestAndSet()) {
            Thread.onSpinWait(); // busy waiting
        }
    }

    @Override
    public void unlock() {
        locked.set(false);
    }
}