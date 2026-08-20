package com.jetfly.roadmarkings;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.core.HolderLookup;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
            new SubProviderEntry(MarkingBlockLootSubProvider::new, LootContextParamSets.BLOCK),
            new SubProviderEntry(SignPoleBlockLootSubProvider::new, LootContextParamSets.BLOCK)
        ), registries);
    }
}