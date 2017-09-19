package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hzliubenlong on 2017/7/22.
 */
public class NoneDeadLockDemo {

    private static ReentrantLock lock1 = new ReentrantLock();
    private static ReentrantLock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        new NoneDeadLockDemo().deadLock();
    }

    private void deadLock() {
        /*
        先锁lock1再锁lock2
         */
        Thread threadA = new Thread(() -> {
            while (true) {
                if (lock1.tryLock()) {
                    try {//休眠1秒，模拟任务执行
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(Thread.currentThread().getName() + " 获得lock1。");
                        if (lock2.tryLock()) {
                            try {
                                System.out.println(Thread.currentThread().getName() + " 获得所有锁，执行完毕。");
                                break;
                            } finally {
                                lock2.unlock();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock1.unlock();
                    }
                }
            }
        });
        Thread threadB = new Thread(() -> {
            while (true) {
                if (lock2.tryLock()) {
                    try {//休眠1秒，模拟任务执行
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(Thread.currentThread().getName() + " 获得lock2。");
                        if (lock1.tryLock()) {
                            try {
                                System.out.println(Thread.currentThread().getName() + " 获得所有锁，执行完毕。");
                                break;
                            } finally {
                                lock1.unlock();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock2.unlock();
                    }
                }
            }
        });
        threadA.start();
        threadB.start();
    }
}
