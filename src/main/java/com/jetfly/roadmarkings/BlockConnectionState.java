package com.jetfly.roadmarkings;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;

public enum BlockConnectionState implements StringRepresentable {
    NONE(0, "none"),
    CONNECTED(1, "connected"),
    DISCONNECTED(2, "disconnected"),
    BASED(3, "based");

    private final int id;
    private final String name;

    private BlockConnectionState(int id, String name) {
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
