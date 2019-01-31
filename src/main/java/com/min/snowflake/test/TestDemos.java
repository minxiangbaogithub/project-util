package com.min.snowflake.test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.min.snowflake.util.SnowflakeIdWorker;

class CreateOrder implements Runnable{
	private static CountDownLatch cdl=new CountDownLatch(50);
//	private static Lock lock=new ReentrantLock();
//	private static Lock lock=new ZookeeperLock();
//	private static SnowflakeIdWorker worker = SnowflakeIdWorker.getInstanceSnowflake();
//	private static SnowFlake snowFlake = new SnowFlake(2, 3);
	private SnowflakeIdWorker worker;
	private static Set<String> set=new HashSet<String>();
	private static Set<SnowflakeIdWorker> set1=new HashSet<SnowflakeIdWorker>();
	public  void run() {
		try {
			cdl.countDown();
			cdl.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		createOrder();
	}
	public CreateOrder(SnowflakeIdWorker worker) {
		this.worker=worker;
	}
	public Set getSet() {
		return this.set;
	}
	public Set getSet1() {
		return this.set1;
	}
	public void createOrder() {
//		synchronized (cdl) {
//			String orderCode=null;
//			lock.lock();
			try {
//				orderCode=String.valueOf(KeyWorker.nextId());
				set.add(String.valueOf(worker.nextId()));
			} catch (Exception e) {
			}finally {
//				lock.unlock();
			}
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
public class TestDemos {
	public static void main(String[] args) throws Exception{
		SnowflakeIdWorker worker = SnowflakeIdWorker.getInstanceSnowflake();
		for(int i=1;i<=500;i++) {
			new Thread(new CreateOrder(worker)).start();
		}
		Thread.sleep(5000);
		Set<String> set = new CreateOrder(worker).getSet();
		System.out.println(set.size());
		System.out.println(new CreateOrder(worker).getSet1().size());
		System.out.println(set);
	}
}

