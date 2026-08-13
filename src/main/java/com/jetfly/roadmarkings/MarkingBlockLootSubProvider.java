package com.jetfly.roadmarkings;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Set;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;

public class MarkingBlockLootSubProvider extends BlockLootSubProvider {
    private static final Set<Item> EXPLOSION_RESISTANT = Set.of();

    public MarkingBlockLootSubProvider(HolderLookup.Provider registries) {
        super(EXPLOSION_RESISTANT, FeatureFlags.DEFAULT_FLAGS, registries);
    }

    private <T extends Block> void dropSelf(Map<String, DeferredBlock<T>> blocks) {
        for(Map.Entry<String, DeferredBlock<T>> b : blocks.entrySet()) {
            super.dropSelf(b.getValue().get());
        }
    }

    @Override
    protected void generate() {
        ModBlocks blocks = RoadMarkings.MOD_BLOCKS;
        dropSelf(blocks.large);
        dropSelf(blocks.small);
        dropSelf(blocks.paint);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return RoadMarkings.BLOCKS.getEntries()
            .stream()
            .map(entry -> (Block) entry.get())
            .toList();
    }
}
