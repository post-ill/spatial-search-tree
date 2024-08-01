package me.post.collection;

import java.util.List;

/**
 * @param <K> The axes value type.
 * @param <V> The holding axes type.
 * @param <P> The finding point type.
 * @param <C> The comparable type of the <code>V</code> type and <code>P</code>, as
 *           the comparable point of the value (usually the minimum point).
 * */
public interface AxisContext<K extends Comparable<K>, V, P, C extends Comparable<C>> {
    int axes();

    K getAxisValue(int axis, V value);

    K getPointAxisValue(int axis, P point);

    C getComparableValue(V value);

    C getComparablePoint(List<K> points);

    boolean isInside(V value, P point);
}
