package org.validcool;

import java.util.*;
import java.util.stream.Stream;

public class CollectionValidations {

    /**
     * Fails when actual collection does not contain at least one item of items.
     */
    public static <E extends Collection<?>> Validator<E> hasItems(E items) {
        return new Validator<>(
                (E value) -> value.containsAll(items),
                String.format("contains %s", Arrays.toString(items.toArray())),
                (E value) -> String.format("%s does not contain %s",
                        Arrays.toString(value.toArray()),
                        Arrays.toString(items.toArray()))
        );
    }

    /**
     * Fails when actual collection contains no item of items.
     * Always wins when items is empty.
     */
    public static <E extends Collection<?>> Validator<E> hasAny(E items) {
        return new Validator<>(
                (E value) -> items.size() == 0 || value.stream().filter(items::contains).findAny().isPresent(),
                String.format("contains any of %s", Arrays.toString(items.toArray())),
                (E value) -> String.format("%s contains none of %s",
                        Arrays.toString(items.toArray()),
                        Arrays.toString(value.toArray()))
        );
    }

    /**
     * Fails when actual collection contains items not in the same order.
     */
    public static <E extends List<?>> Validator<E> hasItemsInOrder(E items) {
        return new Validator<>(
                (E value) -> {
                    // when no items, always "contained in order"
                    if(items.size() == 0) {
                        return true;
                    }
                    Object first = items.get(0);
                    // when only one item, always "contained in order" iff contained
                    if(items.size() == 1) {
                        return value.contains(first);
                    }
                    return findAllIndicesOf(first, (List<Object>)value).anyMatch(
                            (Integer startIndex) -> startIndex + items.size() <= value.size() &&
                                    listEquals(
                                            (List<Object>) value.subList(startIndex, startIndex + items.size()),
                                            (List<Object>) items)
                    );
                },
                String.format("contains %s in same order", Arrays.toString(items.toArray())),
                (E value) -> String.format("%s doesn't contain %s in same order",
                        Arrays.toString(value.toArray()),
                        Arrays.toString(items.toArray()))
        );
    }

    private static Stream<Integer> findAllIndicesOf(Object item, List<Object> list) {
        Queue<Integer> indices = new LinkedList<Integer>();
        for(int i = 0; i < list.size(); i++) {
            if(item.equals(list.get(i))) {
                indices.offer(i);
            }
        }
        return indices.stream();
    }

    private static boolean listEquals(List<Object> a, List<Object> b) {
        if(a == null || b == null) {
            return a == b; // because null == null
        }
        if(a.size() != b.size()) {
            return false;
        }
        for(int i = 0; i < a.size(); i++) {
            Object ita = a.get(i), itb = b.get(i);
            if(ita == itb || ita.equals(itb)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Fails when actual collection contains at least one of the items.
     */
    public static <E extends Collection<?>> Validator<E> hasNot(E items) {
        return new Validator<>(
                (E value) -> items.stream().allMatch((Object it) -> !value.contains(it)),
                String.format("doesn't contain any of %s", Arrays.toString(items.toArray())),
                (E value) -> String.format("%s contains at least one of %s",
                        Arrays.toString(value.toArray()),
                        Arrays.toString(items.toArray()))
        );
    }

    /**
     * Fails when actual and expected collection do not consist of the same items, independent from order.
     */
    public static <E extends Collection<?>> Validator<E> sameItems(E expected) {
        return new Validator<>(
                (E value) -> value.stream().allMatch(expected::contains) && value.size() == expected.size(),
                String.format("contains all the same items as %s", Arrays.toString(expected.toArray())),
                (E value) -> String.format("%s doesn't contain same items as %s",
                        Arrays.toString(value.toArray()),
                        Arrays.toString(expected.toArray()))
        );
    }

    /**
     * Fails when actual and expected collection do not consist of the same items <b>in same order</b>.
     */
    public static <E extends List<?>> Validator<E> sameItemsInOrder(E other) {
        return new Validator<E>(
                (E value) -> listEquals((List<Object>)value, (List<Object>)other),
                String.format("contains same items in same order as %s", Arrays.toString(other.toArray())),
                (E value) -> String.format("%s doesn't contains of %s in same order",
                        Arrays.toString(value.toArray()),
                        Arrays.toString(other.toArray()))
        );
    }

    public static <E extends Collection<?>> Validator<E> hasSize(int size) {
        return new Validator<E>(
                (E value) -> value.size() == size,
                String.format("collection of size %d", size),
                (E value) -> String.format("expected size of %d but got %d", size, value.size())
        );
    }

    public static <E> Validator<Collection<E>> isEmptyCollection() {
        return hasSize(0);
    }

}
