
import java.util.concurrent.atomic.AtomicBoolean;

public class TASLockOp {

    private final AtomicBoolean locked = new AtomicBoolean(false);

    /* Do not modify this method */
    private boolean testAndSet() {
        return locked.getAndSet(true);
    }

    public void lock() {

        while (true) { 
            //check lock with plain read (no expensive atomic)
            while (locked.get()) {

                Thread.onSpinWait(); //busy waiting

            }

            if (!testAndSet()) {//attempt atomic only when lock appears free
                return; //lock acquired
            }

            // this point is only achieved when another thread grabbed the lock between check and testAndSet()
            //loop again and retry
        }
    }

    public void unlock() {
        locked.set(false);
    }
}
