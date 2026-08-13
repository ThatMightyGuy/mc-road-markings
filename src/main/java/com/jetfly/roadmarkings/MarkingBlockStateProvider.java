package com.jetfly.roadmarkings;

import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;

import java.util.Map;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.world.level.block.Block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.PackOutput;

public class MarkingBlockStateProvider extends BlockStateProvider {

    protected static final String BLOCK_PATH = "block/";

    // Parameter values are provided by GatherDataEvent.
    public MarkingBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        // Replace "examplemod" with your own mod id.
        super(output, RoadMarkings.MODID, existingFileHelper);
    }

    private <T extends Block> void registerMarkings(Map<String, DeferredBlock<T>> blocks, String kind) {
        for(Map.Entry<String, DeferredBlock<T>> kvp : blocks.entrySet()) {
            String name = kvp.getKey();

            BlockModelBuilder regular = models().withExistingParent(kind + "_" + name, modLoc(BLOCK_PATH + kind));
            BlockModelBuilder slabbed = models().withExistingParent(kind + "_" + name + "_slabbed", modLoc(BLOCK_PATH + kind + "_slabbed"));

            ResourceLocation tex = modLoc(BLOCK_PATH + name);

            regular.texture("all", tex)
                   .texture("particle", tex);
            slabbed.texture("all", tex)
                   .texture("particle", tex);

            // Similar to #horizontalBlock, but for blocks that are rotatable in all directions, including up and down.
            // Has an overload that accepts a Function<BlockState, ModelFile> instead.
            horizontalBlock(kvp.getValue().get(), state -> Boolean.TRUE.equals(state.getValue(BaseMarkingBlock.SLABBED)) ? slabbed : regular);
        }
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks blocks = RoadMarkings.MOD_BLOCKS;

        registerMarkings(blocks.large, "marking_large");
        registerMarkings(blocks.small, "marking_small");

        for(Map.Entry<String, DeferredBlock<Block>> kvp : blocks.paint.entrySet()) {
            String name = kvp.getKey();

            BlockModelBuilder model = models().withExistingParent("" + name, mcLoc("block/stone"));

            ResourceLocation tex = modLoc(BLOCK_PATH + name);

            model.texture("all", tex);

            simpleBlock(kvp.getValue().get(), model);
        }
    }
}