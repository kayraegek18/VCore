package net.kayega.core.util;

public enum VInventorySize {
    Small(27),
    Medium(45),
    Big(54);

    private final int size;

    VInventorySize(int size) { this.size = size; }

    public int getSize() {
        return size;
    }
}
