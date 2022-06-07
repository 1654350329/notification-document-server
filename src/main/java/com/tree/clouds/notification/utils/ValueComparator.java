package com.tree.clouds.notification.utils;

import java.util.*;
import java.util.stream.Collectors;

public class ValueComparator implements Comparator {

    Map map;
    int type;

    public ValueComparator(Map map, int type) {
        this.map = map;
        this.type = type;
    }

    /**
     * 排序 默认降序
     *
     * @param unsortedMap
     * @param type        0降序 1升序
     * @return
     */
    public static Map sortByValue(Map unsortedMap, int type) {
        Map sortedMap = new TreeMap(new ValueComparator(unsortedMap, type));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    public static <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> aMap, int type) {
        HashMap<K, V> finalOut = new LinkedHashMap<>();
        aMap.entrySet()
                .stream()
                .sorted((p1, p2) -> {
                    if (type == 0) {
                        return p2.getValue().compareTo(p1.getValue());
                    } else {
                        return p1.getValue().compareTo(p2.getValue());
                    }
                })
                .collect(Collectors.toList()).forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
        return finalOut;
    }

    public static Map<String, Integer> countList(Collection<Double> resList) {
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (Double s : resList) {
            //在Map中查找关键字，如果没有就put进Map中，同时value值加1
            //如果有Map中有该元素，说明已经put过了，则直接value值计数加1
            countMap.merge(s.toString(), 1, Integer::sum);
        }
        return countMap;
    }

    public int compare(Object keyA, Object keyB) {
        Comparable valueA = (Comparable) map.get(keyA);
        Comparable valueB = (Comparable) map.get(keyB);
        if (type == 0) {
            return valueB.compareTo(valueA);//这个是降序   升序是valueA.compareTo(valueB)
        } else {
            return valueA.compareTo(valueB);//这个是降序   升序是valueA.compareTo(valueB)
        }
    }
}