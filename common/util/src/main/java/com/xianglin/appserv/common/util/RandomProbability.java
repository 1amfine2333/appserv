/**
 * 
 */
package com.xianglin.appserv.common.util;

import java.math.BigDecimal;
import java.util.Random;

/**
 * 
 * 概率 取得随机数
 * 
 * @author zhangyong 2016年10月9日下午5:03:05
 */
public class RandomProbability {

	private static String rate1 = "cash.packet.rank1";// 0.1-0.2
	private static String rate2 = "cash.packet.rank2";// 0.21-0.3
	private static String rate3 = "cash.packet.rank3";// 0.31-0.4
	private static String rate4 = "cash.packet.rank4";// 0.41-0.5
	
	private static String SEPERATE_M=":";
	private static String SEPERATE_H="-";
	private static final Random RANDOM = new Random();

	
	public static double randomPercent() {
	//	String rate_1 = PropertiesUtil.getProperty(rate1);
	//	String rate_2 = PropertiesUtil.getProperty(rate2);
	//	String rate_3 = PropertiesUtil.getProperty(rate3);
	//	String rate_4 = PropertiesUtil.getProperty(rate4);

		String rate_1 = SysConfigUtil.getStr(rate1, PropertiesUtil.getProperty(rate1));
		String rate_2 = SysConfigUtil.getStr(rate2, PropertiesUtil.getProperty(rate2));
		String rate_3 = SysConfigUtil.getStr(rate3, PropertiesUtil.getProperty(rate3));
		String rate_4 = SysConfigUtil.getStr(rate4, PropertiesUtil.getProperty(rate4));
		String [] rate_1_array = rate_1.split(SEPERATE_M);
		String [] rate_2_array = rate_2.split(SEPERATE_M);
		String [] rate_3_array = rate_3.split(SEPERATE_M);
		String [] rate_4_array = rate_4.split(SEPERATE_M);
		
		double rate_d_1 = getDouble(rate_1_array[0]);
		double rate_d_2 = getDouble(rate_2_array[0]);
		double rate_d_3 = getDouble(rate_3_array[0]);
		double rate_d_4 = getDouble(rate_4_array[0]);
		double percent = RANDOM.nextDouble();
		
	/*	if (percent >= 0 && percent <= rate3) {//如果随机数在0.1 ，则是5毛区间
			return getValue(0.40, 0.50);
		}else if(percent >rate_3 && percent < rate_2+rate_3){
			return getValue(0.30, 0.4);
		}else if(percent >=  rate_2+rate_3 && percent <= rate_1+rate_2+rate_3){
			return getValue(0.20, 0.30);
		}else if(percent >= rate_1+rate_2+rate_3 && percent <= rate_0+rate_1+rate_2+rate_3){
			return getValue(0.10, 0.20);
		}*/
		Double [] d = {0.00,0.00};
		if (percent >= 0 && percent <= rate_d_4) {//如果随机数在0.1 ，则是5毛区间
			d = getDouble(rate_4_array);
		}else if(percent >rate_d_4 && percent <= rate_d_4+rate_d_3){
			d = getDouble(rate_3_array);
		}else if(percent > rate_d_4+rate_d_3 && percent <= rate_d_4+rate_d_3+rate_d_2){
			d = getDouble(rate_2_array);
		}else if(percent >  rate_d_4+rate_d_3+rate_d_2 && percent <= rate_d_4+rate_d_3+rate_d_2+rate_d_1){
			d = getDouble(rate_1_array);
		}
		return getValue( d[0], d[1]);
	}

	
	private static Double getDouble(String doubleStr){
		return Double.valueOf(doubleStr).doubleValue();
	}
	private static Double[] getDouble(String [] doubleStr){
		String [] range = doubleStr[1].split(SEPERATE_H);
		Double [] doubles = new Double[2];
		doubles[0] = Double.valueOf(range[0]).doubleValue();
		doubles[1] = Double.valueOf(range[1]).doubleValue();
		return doubles;
	}
	public static double getValue(double begin, double end) {
		if(end < begin){
			return 0;
		}
		if (begin == end) {
			return begin;
		}
		//使用这个原因是，使用nextDouble 不会得到0和1这两个边界值，现在intNum 会是1-10之间的int值
		int intNum =	RANDOM.nextInt(11);

		Double result = begin+(end-begin) *(Double.valueOf(intNum)/10);

		BigDecimal bigDecimal = BigDecimal.valueOf(result);

		return bigDecimal.setScale(2,BigDecimal.ROUND_DOWN).doubleValue() ;
	}
}
