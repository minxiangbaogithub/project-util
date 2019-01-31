package com.min.lock.zookeeper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderCodeGenerator {
	private static int i=0;
	public String getOrderCode() {
		Date now=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS-");
		return sdf.format(now)+ ++i;
	}
	//单个线程生成的id不会重复
	public static void main(String[] args) {
		OrderCodeGenerator ong=new OrderCodeGenerator();
		for(int i=0;i<10;i++) {
			System.out.println(ong.getOrderCode());
		}
	}
}
