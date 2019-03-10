package com.zwt.framework.utils.util.splitList;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 集合工具类
 */
public class CollectionGroupUtil {
    /**
     * 分割数组，比如有5200数据，按照1000分割，会分为5个1000的list和1个200的list
     * @param list
     * @param quantity
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> groupListByQuantity(List<T> list, int quantity) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Wrong quantity.");
        }
        List<List<T>> wrapList = new ArrayList<List<T>>();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(list.subList(count, (count + quantity) > list.size() ? list.size() : count + quantity));
            count += quantity;
        }
        return wrapList;
    }

}
