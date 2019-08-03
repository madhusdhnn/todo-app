package com.company.api.todoservice.utils;

import java.util.Collections;
import java.util.List;

public class CollectionUtils {

    public static <T extends Comparable<? super T>> List<T> sortList(List<T> list) {
        Collections.sort(list);
        return list;
    }

    public static <T extends Comparable<? super T>> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

}
