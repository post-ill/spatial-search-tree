package me.post.collection;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface SpatialSearchTree<V, P> extends Collection<V> {
    @Nullable V search(P point);

    default boolean has(P point) {
        return search(point) != null;
    }
}
