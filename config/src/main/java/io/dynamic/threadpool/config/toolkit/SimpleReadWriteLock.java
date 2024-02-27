package io.dynamic.threadpool.config.toolkit;

/**
 * 简单读写锁.
 */
public class SimpleReadWriteLock {

    public synchronized boolean tryReadLock() {
        if (isWriteLocked()) {
            return false;
        } else {
            status++;
            return true;
        }
    }

    public synchronized void releaseReadLock() {
        status--;
    }

    public synchronized boolean tryWriteLock() {
        if (!isFree()) {
            return false;
        } else {
            status = -1;
            return true;
        }
    }

    public synchronized void releaseWriteLock() {
        status = 0;
    }

    private boolean isWriteLocked() {
        return status < 0;
    }

    private boolean isFree() {
        return status == 0;
    }

    /**
     * 零表示无锁;负数表示写锁;正数表示读锁，数值表示读锁的数量。
     */
    private int status = 0;
}
