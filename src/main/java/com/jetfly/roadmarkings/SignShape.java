package com.jetfly.roadmarkings;

import net.minecraft.util.StringRepresentable;

public enum SignShape implements StringRepresentable{
    SQUARE(0, "square"),
    CIRCLE(1, "circle"),
    TRIANGLE(2, "triangle"),
    OCTAGON(3, "octagon"),
    RHOMBUS(4, "rhombus");

    private final int id;
    private final String name;

    private SignShape(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
