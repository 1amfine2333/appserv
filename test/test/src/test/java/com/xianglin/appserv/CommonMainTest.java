package com.xianglin.appserv;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.RandomUtils;

public class CommonMainTest {
	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 10000; i++) {
			System.err.println(new BigDecimal(RandomUtils.nextDouble(0.1, 0.5)).setScale(2, RoundingMode.HALF_UP).doubleValue());
		}
	}
}
