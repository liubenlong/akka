package lock;

import java.util.concurrent.TimeUnit;

/**
 * Created by hzliubenlong on 2017/7/22.
 */
public class LockDemo {

    private static String lock1 = "lock1";
    private static String lock2 = "lock2";

    public static void main(String[] args) {
        new LockDemo().deadLock();
    }

    private void deadLock() {
        /*
        先锁lock1再锁lock2
         */
        Thread threadA = new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " 获得lock1。");

                try {//休眠1秒，模拟任务执行
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " 获得所有锁，执行完毕。");
                }
            }
        });
        Thread threadB = new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " 获得lock2。");

                try {//休眠1秒，模拟任务执行
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + " 获得所有锁，执行完毕。");
                }
            }
        });
        threadA.start();
        threadB.start();
    }
}
