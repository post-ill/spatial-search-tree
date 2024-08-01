package me.post.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class AxisByAxisTree<K extends Comparable<K>, V, P, C extends Comparable<C>> implements SpatialSearchTree<V, P> {
    private final AxisContext<K, V, P, C> context;
    private final NavigableMap<K, Integer>[] sortedAxes;
    private final NavigableMap<C, V> comparableValues;

    @SuppressWarnings("unchecked")
    public AxisByAxisTree(AxisContext<K, V, P, C> context, Supplier<NavigableMap<C, V>> mapSupplier) {
        this.context = context;
        sortedAxes = new NavigableMap[context.axes()];
        comparableValues = mapSupplier.get();

        for (int axis = 0; axis < context.axes(); axis++) {
            sortedAxes[axis] = new TreeMap<>();
        }
    }

    public AxisByAxisTree(AxisContext<K, V, P, C> context) {
        this(context, TreeMap::new);
    }

    @Override
    public @NotNull Iterator<V> iterator() {
        return comparableValues.values().iterator();
    }

    @Override
    public Object @NotNull[] toArray() {
        return comparableValues.values().toArray();
    }

    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] array) {
        return comparableValues.values().toArray(array);
    }

    @Override
    public int size() {
        return comparableValues.size();
    }

    @Override
    public boolean isEmpty() {
        return comparableValues.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        return comparableValues.containsKey(context.getComparableValue((V) o));
    }

    @Override
    public boolean add(V value) {
        final C comparablePoint = context.getComparableValue(value);

        if (comparableValues.containsKey(comparablePoint)) {
            return false;
        }

        for (int axis = 0; axis < sortedAxes.length; axis++) {
            final K axisValue = context.getAxisValue(axis, value);
            sortedAxes[axis].put(axisValue, sortedAxes[axis].getOrDefault(axisValue, 0) + 1);
        }

        comparableValues.put(comparablePoint, value);
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends V> collection) {
        boolean anyAdd = false;

        for (V value : collection) {
            if (add(value)) {
                anyAdd = true;
            }
        }

        return anyAdd;
    }

    @Override
    public @Nullable V search(P point) {
        final List<K> queue = new ArrayList<>(context.axes());

        for (int axis = 0; axis < context.axes(); axis++) {
            final K nearestDownPoint = sortedAxes[axis].floorKey(context.getPointAxisValue(axis, point));

            if (nearestDownPoint == null) {
                return null;
            }

            queue.add(nearestDownPoint);
        }

        final V value = comparableValues.get(context.getComparablePoint(queue));

        if (value == null) {
            return null;
        }

        return context.isInside(value, point) ? value : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object value) {
        final boolean removed = comparableValues.remove(context.getComparableValue((V) value)) != null;

        if (removed) {
            for (int axis = 0; axis < sortedAxes.length; axis++) {
                final K axisValue = context.getAxisValue(axis, (V) value);
                final int valuesLocated = sortedAxes[axis].get(axisValue);

                if (valuesLocated == 1) {
                    sortedAxes[axis].remove(axisValue);
                    continue;
                }

                sortedAxes[axis].put(axisValue, valuesLocated - 1);
            }
        }

        return removed;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        int containsCount = 0;

        for (Object value : collection) {
            if (contains(value)) {
                containsCount++;
            }
        }

        return containsCount == collection.size();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        boolean anyRemove = false;

        for (Object value : collection) {
            if (remove(value)) {
                anyRemove = true;
            }
        }

        return anyRemove;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        final Map<C, V> retained = new HashMap<>();
        final Collection<V> removed = new ArrayList<>();

        collection.forEach(value -> {
            if (!contains(value)) {
                return;
            }

            retained.put(context.getComparableValue((V) value), (V) value);
        });

        comparableValues.forEach((key, value) -> {
            if (retained.containsKey(key)) {
                return;
            }

            removed.add(value);
        });

        removeAll(removed);
        return true;
    }

    @Override
    public void clear() {
        comparableValues.clear();

        for (NavigableMap<K, Integer> sortedAxis : sortedAxes) {
            sortedAxis.clear();
        }
    }
}
