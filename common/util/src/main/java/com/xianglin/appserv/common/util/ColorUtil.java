package com.xianglin.appserv.common.util;

import java.util.HashMap;
import java.util.Map;

public class ColorUtil {
	public static String getColor(int key){
		Map<Integer ,String> map=new HashMap<>();
		map.put(1, "#2e96ff");map.put(31, "#633808");
		map.put(2, "#6beeca");map.put(32, "#27beae");
		map.put(3, "#ffcc35");map.put(33, "#ffb337");
		map.put(4, "#f87a78");map.put(34, "#e6014f");
		map.put(5, "#aba9fd");map.put(35, "#b766da");
		map.put(6, "#ff72f8");map.put(36, "#339079");
		map.put(7, "#c9c9c9");map.put(37, "#efb8d0");
		map.put(8, "#f19fc2");map.put(38, "#d1c399");
		map.put(9, "#aedd4a");map.put(39, "#2f211e");
		map.put(10, "#cfa971");map.put(40, "#628d0a");
		map.put(11, "#7ecff3");map.put(41, "#2eff64");
		map.put(12, "#80c269");map.put(42, "#013526");
		map.put(13, "#f19d76");map.put(43, "#a1a1a1");
		map.put(14, "#7b0100");map.put(44, "#594a3e");
		map.put(15, "#c391bf");map.put(45, "#a3025b");
		map.put(16, "#005a82");map.put(46, "#b6aa00");
		map.put(17, "#31b26b");map.put(47, "#a6ba75");
		map.put(18, "#fff101");map.put(48, "#ffcc35");
		map.put(19, "#e60111");map.put(49, "#fb7cbc");
		map.put(20, "#ae5fa0");map.put(50, "#59cfca");
		map.put(21, "#006ab7");map.put(51, "#b685db");
		map.put(22, "#087c22");map.put(52, "#1f5b3d");
		map.put(23, "#858200");map.put(53, "#e8deb5");
		map.put(24, "#7e6c5b");map.put(54, "#636363");
		map.put(25, "#4f0146");map.put(55, "#1412b9");
		map.put(26, "#0076a9");map.put(56, "#a8a55b");
		map.put(27, "#cfa971");map.put(57, "#4ba890");
		map.put(28, "#86a443");map.put(58, "#988242");
		map.put(29, "#b46866");map.put(59, "#8e2620");
		map.put(30, "#9595c8");map.put(60, "#1e1b5f");
		return map.get(key);
	}
}
