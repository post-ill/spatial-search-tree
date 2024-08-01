package me.post.collection.util;

import org.jetbrains.annotations.NotNull;

/**
 * Example of a comparable 2D point.
 * */
public class IntPair implements Comparable<IntPair> {
    private final int x;
    private final int y;

    public IntPair(int x, int y, int z) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(@NotNull IntPair pair) {
        if (y < pair.y) {
            return -1;
        }

        if (y > pair.y) {
            return 1;
        }

        if (x < pair.x) {
            return -1;
        }

        return x > pair.x ? 1 : 0;
    }
}
