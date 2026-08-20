package com.jetfly.roadmarkings;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;

public class SignPoleBlockLootSubProvider extends BlockLootSubProvider {
    private static final Set<Item> EXPLOSION_RESISTANT = Set.of();

    public SignPoleBlockLootSubProvider(HolderLookup.Provider registries) {
        super(EXPLOSION_RESISTANT, FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected void generate() {
        dropSelf(RoadMarkings.SIGN_POLE_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        // return RoadMarkings.BLOCKS.getEntries()
        //     .stream()
        //     .map(entry -> (Block) entry.get())
        //     .toList();

        return Arrays.asList(RoadMarkings.SIGN_POLE_BLOCK.get());
    }
}
