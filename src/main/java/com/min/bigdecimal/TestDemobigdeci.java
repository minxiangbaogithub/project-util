package com.min.bigdecimal;

import java.math.BigDecimal;

public class TestDemobigdeci {
	public static void main(String[] args) {
    BigDecimal b1=new BigDecimal("5.5333");
    BigDecimal b2=new BigDecimal(100);
    System.out.println(b1+","+b2);
    System.out.println("相加："+b1.add(b2));
    System.out.println("相减："+b1.subtract(b2));
    System.out.println("相乘："+b1.multiply(b2));
    System.out.println("相除："+b1.divide(b2));
    //此时5.3333精确到小数点后三位以内会丢失精度报错
    System.out.println("精确位数："+b1.setScale(4));//保留小数位
    //删除多余小数位，结果是5.533
    System.out.println("精确位数："+b1.setScale(3,BigDecimal.ROUND_DOWN));
    //进位处理,结果5.534
    System.out.println("精确位数："+b1.setScale(3,BigDecimal.ROUND_UP));
    //四舍五入向上舍（够5进一）-5.533
    System.out.println("精确位数："+b1.setScale(3,BigDecimal.ROUND_HALF_UP));
    //向下舍（够6进一）-5.533
    System.out.println("精确位数："+b1.setScale(3,BigDecimal.ROUND_HALF_DOWN));
	}
}
