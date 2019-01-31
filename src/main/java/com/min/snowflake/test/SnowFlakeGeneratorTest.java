package com.min.snowflake.test;

import com.min.snowflake.util.SnowFlakeGenerator;
//示例二
public class SnowFlakeGeneratorTest {

	public static void main(String[] args) {
		SnowFlakeGenerator snowflake=new SnowFlakeGenerator.Factory().create(11111, 22222);
		System.out.println("生成序列号："+snowflake.nextId());
	}
}
