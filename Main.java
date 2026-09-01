import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final int NUMBER_OF_THREADS = 16;
    private static final int INCREMENTS_PER_THREAD = 1000000;
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new TASLockOp();
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        long startTime = System.nanoTime();
        AtomicInteger totalTestAndSetCount = new AtomicInteger(0);

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    lock.lock();
                    counter++;
                    lock.unlock();
                }

                // add total getAndSet() calls for this thread
                totalTestAndSetCount.addAndGet(lock.getTestAndSetCount());
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long endTime = System.nanoTime();

        System.out.println("Expected counter: " + (NUMBER_OF_THREADS * INCREMENTS_PER_THREAD));
        System.out.println("Actual counter: " + counter);
        System.out.println("Execution time: " + (endTime - startTime) / 1000000 + " ms");
        System.out.println("Total testAndSet() calls: " + totalTestAndSetCount.get());
    }
}