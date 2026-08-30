package com.jetfly.roadmarkings;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

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
        ModBlocks blocks = RoadMarkings.MOD_BLOCKS;

        // This used to return a list of the entire mod blocks registry
        // That did not work out for me, so I decided to explicitly list everything
        return Stream.concat(
            Stream.concat(
                blocks.large.values().stream(),
                blocks.small.values().stream()
            ).map(x -> (Block) x.get()),
            blocks.paint.values().stream().map(x -> x.get())
        ).toList();
    }
}