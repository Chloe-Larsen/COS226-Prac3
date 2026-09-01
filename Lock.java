public abstract class Lock {

    private final ThreadLocal<Integer> testAndSetCount = new ThreadLocal<>();

    protected void incrementTestAndSetCount() {
        Integer current = testAndSetCount.get();
        if (current == null)
            current = 0;
        testAndSetCount.set(current + 1);
    }

    public int getTestAndSetCount() {
        return testAndSetCount.get();
    }

    abstract void lock();

    abstract void unlock();
}
