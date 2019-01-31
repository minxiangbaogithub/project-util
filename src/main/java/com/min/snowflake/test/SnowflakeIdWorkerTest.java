package com.min.snowflake.test;

import com.min.snowflake.util.SnowflakeIdWorker;
//示例三
public class SnowflakeIdWorkerTest {

	public static void main(String[] args) {
		//单例实现
        SnowflakeIdWorker work=SnowflakeIdWorker.getInstanceSnowflake();
        try {
			System.out.println("生成序列号："+work.nextId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
