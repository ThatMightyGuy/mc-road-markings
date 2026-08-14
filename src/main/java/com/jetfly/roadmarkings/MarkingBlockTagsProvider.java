package com.jetfly.roadmarkings;

import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;
import java.util.Map;
import net.minecraft.tags.BlockTags;



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

        for(DeferredBlock<Block> block : RoadMarkings.MOD_BLOCKS.paint.values()) {
            Block b = block.get();
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(b).replace(false);
        }
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

    private <T extends Block> void addMarkingColorTag(Map<DyeColor, TagKey<Block>> tags, String[] patterns, Map<String, DeferredBlock<T>> pool) {
        for (DyeColor dye : DyeColor.values()) {
            for(String target : patterns) {
                T block = pool.get(target + "_" + dye).get();
                tag(getDyedTag(dye)).add(block);
                tag(tags.get(dye)).add(block);
            }
        }
    }
}
