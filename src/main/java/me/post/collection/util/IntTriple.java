package me.post.collection.util;

import org.jetbrains.annotations.NotNull;

/**
 * Example of a comparable 3D point.
 * */
public class IntTriple implements Comparable<IntTriple> {
    private final int x;
    private final int y;
    private final int z;

    public IntTriple(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int compareTo(@NotNull IntTriple triple) {
        if (z < triple.z) {
            return -1;
        }

        if (z > triple.z) {
            return 1;
        }

        if (y < triple.y) {
            return -1;
        }

        if (y > triple.y) {
            return 1;
        }

        if (x < triple.x) {
            return -1;
        }

        return x > triple.x ? 1 : 0;
    }
}
