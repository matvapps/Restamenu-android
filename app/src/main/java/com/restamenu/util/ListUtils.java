package com.restamenu.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Roodie
 */

public class ListUtils {

    public static <V> int getSize(List<V> sourceList) {
        return sourceList == null ? 0 : sourceList.size();
    }


    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);

    }

    public static <T> Collection<T> filter(Collection<T> target, Predicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element : target) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public static <T> boolean listEqualsNoOrder(List<T> l1, List<T> l2) {
        final Set<T> s1 = new HashSet<>(l1);
        final Set<T> s2 = new HashSet<>(l2);

        return s1.equals(s2);
    }

    public static <T> boolean listEqualsIgnoringOrder(List<T> list1, List<T> list2, Comparator<? super T> comparator) {

        // if not the same size, lists are not equal
        if (list1.size() != list2.size()) {
            return false;
        }

        // create sorted copies to avoid modifying the original lists
        List<T> copy1 = new ArrayList<>(list1);
        List<T> copy2 = new ArrayList<>(list2);

        Collections.sort(copy1, comparator);
        Collections.sort(copy2, comparator);

        // iterate through the elements and compare them one by one using
        // the provided comparator.
        Iterator<T> it1 = copy1.iterator();
        Iterator<T> it2 = copy2.iterator();
        while (it1.hasNext()) {
            T t1 = it1.next();
            T t2 = it2.next();
            if (comparator.compare(t1, t2) != 0) {
                // as soon as a difference is found, stop looping
                return false;
            }
        }
        return true;
    }

    /**
     * Returns if both {@link Collection Collections} contains the same elements, in the same quantities, regardless of order and collection type.
     * <p>
     * Empty collections and {@code null} are regarded as equal.
     */
    public static <T> boolean haveSameElements(Collection<T> col1, Collection<T> col2) {
        if (col1 == col2)
            return true;

        // If either list is null, return whether the other is empty
        if (col1 == null)
            return col2.isEmpty();
        if (col2 == null)
            return col1.isEmpty();

        // If lengths are not equal, they can't possibly match
        if (col1.size() != col2.size())
            return false;

        // Helper class, so we don't have to do a whole lot of autoboxing
        class Count {
            // Initialize as 1, as we would increment it anyway
            public int count = 1;
        }

        final Map<T, Count> counts = new HashMap<>();

        // Count the items in list1
        for (final T item : col1) {
            final Count count = counts.get(item);
            if (count != null)
                count.count++;
            else
                // If the map doesn't contain the item, put a new count
                counts.put(item, new Count());
        }

        // Subtract the count of items in list2
        for (final T item : col2) {
            final Count count = counts.get(item);
            // If the map doesn't contain the item, or the count is already reduced to 0, the lists are unequal
            if (count == null || count.count == 0)
                return false;
            count.count--;
        }

        // If any count is nonzero at this point, then the two lists don't match
        for (final Count count : counts.values())
            if (count.count != 0)
                return false;

        return true;
    }

}
