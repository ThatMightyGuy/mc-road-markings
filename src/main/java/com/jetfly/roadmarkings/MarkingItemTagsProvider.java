package com.jetfly.roadmarkings;

import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;
import java.util.Map;

import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import org.jetbrains.annotations.Nullable;

public class MarkingItemTagsProvider extends ItemTagsProvider {

    public MarkingItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, blockTags, RoadMarkings.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        copyTags(RoadMarkings.TAGS_MARKING_LARGE, RoadMarkings.ITEM_TAGS_MARKING_LARGE);
        copyTags(RoadMarkings.TAGS_MARKING_SMALL, RoadMarkings.ITEM_TAGS_MARKING_SMALL);
        copyTags(RoadMarkings.TAGS_MARKING_PAINT, RoadMarkings.ITEM_TAGS_MARKING_PAINT);
    }

    private void copyTags(Map<DyeColor, TagKey<Block>> blockTags, Map<DyeColor, TagKey<Item>> itemTags) {
        for (DyeColor color : DyeColor.values()) {
            copy(
                blockTags.get(color),
                itemTags.get(color)
            );
        }
    }
}
