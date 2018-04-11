package com.ai.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtils {

	/**
	 * 从List中随机抽取N个元素
	 * 
	 * @param list
	 * @param n
	 * @return
	 */
	public static <T> List<T> createRandomList(List<T> list, int n) {
		if (list == null || list.size() <= n) {
			return list;
		} else {
			List<T> listNew = new ArrayList<T>();
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			while (map.size() < n) {
				int random = (int) (Math.random() * list.size());
				if (!map.containsKey(random)) {
					map.put(random, 1);
					listNew.add(list.get(random));
				}
			}
			return listNew;
		}
	}

	/**
	 * 从List中获取前面N个元素
	 * 
	 * @param list
	 * @param n
	 * @return
	 */
	public static <T> List<T> createSubList(List<T> list, int n) {
		if (list == null || list.size() <= n) {
			return list;
		} else {
			return list.subList(0, n);
		}
	}
}
