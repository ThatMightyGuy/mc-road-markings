package com.jetfly.roadmarkings;

import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nonnull;


public class MarkingBlockTagsProvider extends BlockTagsProvider {

    public static final TagKey<Block> TAG_CREATE_MOVABLE_EMPTY_COLLIDER = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("create", "movable_empty_collider"));
    public static final TagKey<Block> TAG_BASE_MARKING_LARGE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("roadmarkings", "marking_large"));
    public static final TagKey<Block> TAG_BASE_MARKING_SMALL = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("roadmarkings", "marking_small"));
    public static final TagKey<Block> TAG_BASE_ASPHALT = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("roadmarkings", "asphalt"));

    public static final TagKey<Item> TAG_WRENCH = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tools/wrench"));

    public static final Map<DyeColor, TagKey<Block>> TAGS_MARKING_LARGE = generateColorTags("marking_large");
    public static final Map<DyeColor, TagKey<Block>> TAGS_MARKING_SMALL = generateColorTags("marking_small");
    public static final Map<DyeColor, TagKey<Block>> TAGS_MARKING_PAINT = generateColorTags("asphalt");

    private static Map<DyeColor, TagKey<Block>> generateColorTags(@Nonnull String baseTag) {
        Map<DyeColor, TagKey<Block>> tags = new EnumMap<>(DyeColor.class);

        for(DyeColor color : DyeColor.values()) {
            TagKey<Block> tag = TagKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(RoadMarkings.MODID, baseTag + "/" + color.getSerializedName())
            );

            tags.put(color, tag);
        }

        return tags;
    }

    public MarkingBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, RoadMarkings.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addMarkingColorTag(TAGS_MARKING_LARGE, RoadMarkings.PATTERNS_LARGE, RoadMarkings.MOD_BLOCKS.large, TAG_BASE_MARKING_LARGE);
        addMarkingColorTag(TAGS_MARKING_SMALL, RoadMarkings.PATTERNS_SMALL, RoadMarkings.MOD_BLOCKS.small, TAG_BASE_MARKING_SMALL);
        addMarkingColorTag(TAGS_MARKING_PAINT, new String[]{"asphalt"}    , RoadMarkings.MOD_BLOCKS.paint, TAG_BASE_ASPHALT);

        tag(TAG_CREATE_MOVABLE_EMPTY_COLLIDER)
            .add(TagEntry.tag(TAG_BASE_MARKING_LARGE.location()))
            .add(TagEntry.tag(TAG_BASE_MARKING_SMALL.location()));

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(TagEntry.tag(TAG_BASE_ASPHALT.location()));
    }

    private static TagKey<Block> getDyedTag(DyeColor color) {
        return switch (color) {
            case WHITE      -> Tags.Blocks.DYED_WHITE;
            case ORANGE     -> Tags.Blocks.DYED_ORANGE;
            case MAGENTA    -> Tags.Blocks.DYED_MAGENTA;
            case LIGHT_BLUE -> Tags.Blocks.DYED_LIGHT_BLUE;
            case YELLOW     -> Tags.Blocks.DYED_YELLOW;
            case LIME       -> Tags.Blocks.DYED_LIME;
            case PINK       -> Tags.Blocks.DYED_PINK;
            case GRAY       -> Tags.Blocks.DYED_GRAY;
            case LIGHT_GRAY -> Tags.Blocks.DYED_LIGHT_GRAY;
            case CYAN       -> Tags.Blocks.DYED_CYAN;
            case PURPLE     -> Tags.Blocks.DYED_PURPLE;
            case BLUE       -> Tags.Blocks.DYED_BLUE;
            case BROWN      -> Tags.Blocks.DYED_BROWN;
            case GREEN      -> Tags.Blocks.DYED_GREEN;
            case RED        -> Tags.Blocks.DYED_RED;
            case BLACK      -> Tags.Blocks.DYED_BLACK;
            default         -> Tags.Blocks.DYED;
        };
    }

    private <T extends Block> void addMarkingColorTag(Map<DyeColor, TagKey<Block>> tags, String[] patterns, Map<String, DeferredBlock<T>> pool, TagKey<Block> baseTag) {
        for (DyeColor dye : DyeColor.values()) {
            TagKey<Block> coloredBaseTag = tags.get(dye);
            for(String target : patterns) {
                T block = pool.get(target + "_" + dye).get();
                tag(coloredBaseTag).add(block);
            }
            TagEntry baseTagEntry = TagEntry.tag(coloredBaseTag.location());
            tag(getDyedTag(dye)).add(baseTagEntry);
            tag(baseTag).add(baseTagEntry);
        }
    }
}