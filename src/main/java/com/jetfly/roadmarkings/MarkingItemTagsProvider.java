package com.jetfly.roadmarkings;

import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;
import java.util.EnumMap;
import java.util.Map;

import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import org.jetbrains.annotations.Nullable;
import javax.annotation.Nonnull;

public class MarkingItemTagsProvider extends ItemTagsProvider {
    public static final Map<DyeColor, TagKey<Item>> ITEM_TAGS_MARKING_LARGE = generateItemTags(MarkingBlockTagsProvider.TAGS_MARKING_LARGE);
    public static final Map<DyeColor, TagKey<Item>> ITEM_TAGS_MARKING_SMALL = generateItemTags(MarkingBlockTagsProvider.TAGS_MARKING_SMALL);
    public static final Map<DyeColor, TagKey<Item>> ITEM_TAGS_MARKING_PAINT = generateItemTags(MarkingBlockTagsProvider.TAGS_MARKING_PAINT);

    private static Map<DyeColor, TagKey<Item>> generateItemTags(Map<DyeColor, TagKey<Block>> tags) {
        Map<DyeColor, TagKey<Item>> result = new EnumMap<>(DyeColor.class);
        for(Map.Entry<DyeColor, TagKey<Block>> tag : tags.entrySet()) {
            result.put(tag.getKey(), TagKey.create(Registries.ITEM, tag.getValue().location()));
        }
        return result;
    }

    public MarkingItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, blockTags, RoadMarkings.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {

        copyTags(MarkingBlockTagsProvider.TAGS_MARKING_LARGE, ITEM_TAGS_MARKING_LARGE);
        copyTags(MarkingBlockTagsProvider.TAGS_MARKING_SMALL, ITEM_TAGS_MARKING_SMALL);
        copyTags(MarkingBlockTagsProvider.TAGS_MARKING_PAINT, ITEM_TAGS_MARKING_PAINT);
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