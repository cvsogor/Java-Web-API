package com.aditor.bi.commercials.domain.util;

import com.google.common.collect.Sets;

import java.util.Set;


public class SetUtils {
    public static boolean isSuperSet(Set<String> superset, Set<String> subset) {
        return Sets.difference(superset, subset).size() > 0 &&
                Sets.intersection(superset, subset).size() > 0 &&
                Sets.difference(subset, superset).size() == 0;
    }

    public static boolean isDisjoint(Set<String> left, Set<String> right) {
       return Sets.intersection(left, right).size() == 0;
    }

    public static boolean isEqual(Set<String> left, Set<String> right) {
        return Sets.difference(left, right).size() == 0 && Sets.difference(right, left).size() == 0;
    }
}
