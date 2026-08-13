package com.jetfly.roadmarkings;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlocks {
    public final Map<String, DeferredBlock<LargeMarkingBlock>> large = new HashMap<>();
    public final Map<String, DeferredBlock<SmallMarkingBlock>> small = new HashMap<>();
    public final Map<String, DeferredBlock<Block>> paint             = new HashMap<>();
}
