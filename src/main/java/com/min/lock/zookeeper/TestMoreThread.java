package com.min.lock.zookeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CreateOrder implements Runnable{
	private static OrderCodeGenerator ong=new OrderCodeGenerator();
	private static CountDownLatch cdl=new CountDownLatch(50);
//	private static Lock lock=new ReentrantLock();
	private static Lock lock=new ZookeeperLock();
	public void run() {
		try {
			cdl.countDown();
			cdl.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		createOrder();
	}
	public void createOrder() {
//		synchronized (cdl) {
			String orderCode=null;
			lock.lock();
			try {
				orderCode=ong.getOrderCode();
			} catch (Exception e) {
			}finally {
				lock.unlock();
			}
			System.out.println(Thread.currentThread().getName()+"======>"+orderCode);
//		}
	}
	
}
/**
 * 多线程的情况下，生成的id会重复，可以使用synchronized或是加Lock锁（ReentrantLock）
 * 这两种情况只能解决单个jvm中的同步问题，如果是分布是系统中，
 * 只能使用分布式锁来实现，zookeeper实现分布式锁性能太低
 * @author xiangbao.min
 *
 */
public class TestMoreThread {
	public static void main(String[] args) {
		for(int i=1;i<=50;i++) {
			new Thread(new CreateOrder()).start();
		}
	}
}
