import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class TASLockOp extends Lock {

    private final AtomicBoolean locked = new AtomicBoolean(false);

    private static final int MIN_DELAY_NS = 10;
    private static final int MAX_DELAY_NS = 1000;

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
        int delay = MIN_DELAY_NS;

        while (true) {
            // check lock with plain read (no expensive atomic)
            while (locked.get()) {
                Thread.onSpinWait(); // busy waiting
            }

            // attempt atomic only when lock appears free
            if (!doTestAndSet()) {
                return; // lock acquired
            }

            // this point is only achieved when another thread grabbed the lock between
            // check and testAndSet() - back off and then loop again to retry

            int backoff = ThreadLocalRandom.current().nextInt(delay);
            long deadline = System.nanoTime() + backoff;

            while (System.nanoTime() < deadline) {
                Thread.onSpinWait(); // do backoff wait
            }

            // exponentially increase backoff limit up to MAX_DELAY_NS
            delay = Math.min(MAX_DELAY_NS, delay * 2);
        }
    }

    @Override
    public void unlock() {
        locked.set(false);
    }
}