package com.jetfly.roadmarkings;

import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import org.jetbrains.annotations.Nullable;



public class MarkingBlockTagsProvider extends BlockTagsProvider {

    public MarkingBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, RoadMarkings.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        addMarkingColorTag(RoadMarkings.TAGS_MARKING_LARGE, RoadMarkings.PATTERNS_LARGE, RoadMarkings.MOD_BLOCKS.large);
        addMarkingColorTag(RoadMarkings.TAGS_MARKING_SMALL, RoadMarkings.PATTERNS_SMALL, RoadMarkings.MOD_BLOCKS.small);
        addMarkingColorTag(RoadMarkings.TAGS_MARKING_PAINT, new String[]{"asphalt"}    , RoadMarkings.MOD_BLOCKS.paint);
    }

    private <T extends Block> void addMarkingColorTag(Map<DyeColor, TagKey<Block>> tags, String[] patterns, Map<String, DeferredBlock<T>> pool) {
        for (DyeColor dye : DyeColor.values()) {
            for(String target : patterns) {
                DeferredBlock<T> block = pool.get(target + "_" + dye);
                tag(tags.get(dye)).add(block.get());
            }
        }
    }
}
