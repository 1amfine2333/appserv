/**
 * 
 */
package com.xianglin.appserv.common.util;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * 
 * @author wanglei 2016年11月30日上午10:26:57
 */
public class CollectionUtils {

	/**
	 * @param source
	 * @param startIndex
	 * @param endIndex
	 * @param <T>
	 * @return
	 */
	public static <T extends Collection<?>> T remove(T source,int startIndex,int endIndex){
		Iterator<?> iter = source.iterator();
		int i = 0;
		while(iter.hasNext()){
			iter.next();
		    if(i>= startIndex && i < endIndex){
		    	iter.remove();
		    }
		    i ++ ;
		}
		return source;
	}

	/**对集合内容进行随机
	 * @param source
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> randomList(List<T> source){
		List<T> result = new ArrayList<>(source.size());
		while (source.size() > 0){
			int index = RandomUtils.nextInt(0,source.size());
			result.add(source.get(index));
			source.remove(index);
		}
		return result;
	}
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("99");
//		System.out.println(remove(list, 3, list.size()-1));
		System.out.println(randomList(list));
	}
}
