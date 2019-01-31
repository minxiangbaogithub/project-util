package com.min.lock.zookeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
//zookeeper实现分布式锁
public class ZookeeperLock implements Lock {

	private static final String zk_ip_port="192.168.129.121:2181";
    private static final String lock_node="/LOCK";
    private ZkClient zkClient=new ZkClient(zk_ip_port);
    private CountDownLatch cdl=null;
	public void lock() {
         if(tryLock()) {
        	 return;
         }
         waitForlock();
         lock();
	}
    private void waitForlock() {
    	IZkDataListener listener=new IZkDataListener() {
			
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("删除节点"+dataPath);
				if(cdl!=null) {
					cdl.countDown();
				}
			}
			
			public void handleDataChange(String dataPath, Object data) throws Exception {
				
			}
		};
    	zkClient.subscribeDataChanges(lock_node, listener);
    	if(zkClient.exists(lock_node)) {
    		try {
    			cdl=new CountDownLatch(1);
    			cdl.await();
    		}catch (Exception e) {
			}
    	}
    	zkClient.unsubscribeDataChanges(lock_node, listener);
    }
	public void lockInterruptibly() throws InterruptedException {

	}

	public boolean tryLock() {
		try {
			zkClient.createEphemeral(lock_node);
//			zkClient.createPersistent(lock_node);
			return true;
		} catch (ZkNodeExistsException e) {
			return false;
		}
		
	}

	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	public void unlock() {
		zkClient.delete(lock_node);
	}

	public Condition newCondition() {
		return null;
	}


}
