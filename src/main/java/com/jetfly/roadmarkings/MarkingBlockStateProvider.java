package com.jetfly.roadmarkings;

import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;

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

    private void registerSignPoles() {
        ResourceLocation tex = modLoc("block/asphalt_light_gray");

        BlockModelBuilder core = models().withExistingParent("sign_post_metal_core", modLoc(BLOCK_PATH + "sign_post_core"));
        core.texture("all", tex);
        core.texture("particle", tex);
        BlockModelBuilder nibble = models().withExistingParent("sign_post_metal_nibble", modLoc(BLOCK_PATH + "sign_post_nibble"));
        nibble.texture("all", tex);
        nibble.texture("particle", tex);
        BlockModelBuilder base = models().withExistingParent("sign_post_metal_base", modLoc(BLOCK_PATH + "sign_post_base"));
        base.texture("all", tex);
        base.texture("particle", tex);

        SignPoleBlock block = RoadMarkings.SIGN_POLE_BLOCK.get();

        MultiPartBlockStateBuilder multipartBuilder = getMultipartBuilder(block);
        multipartBuilder
            .part()
                .modelFile(core)
                .addModel()
            .end()
            .part()
                .modelFile(nibble)
                .rotationX(270)
                .addModel()
                .condition(SignPoleBlock.NORTH, BlockConnectionState.CONNECTED, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(nibble)
                .rotationX(90)
                .addModel()
                .condition(SignPoleBlock.SOUTH, BlockConnectionState.CONNECTED, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(nibble)
                .rotationX(90)
                .rotationY(270)
                .addModel()
                .condition(SignPoleBlock.EAST, BlockConnectionState.CONNECTED, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(nibble)
                .rotationX(90)
                .rotationY(90)
                .addModel()
                .condition(SignPoleBlock.WEST, BlockConnectionState.CONNECTED, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(nibble)
                .rotationX(180)
                .addModel()
                .condition(SignPoleBlock.UP, BlockConnectionState.CONNECTED, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(nibble)
                .addModel()
                .condition(SignPoleBlock.DOWN, BlockConnectionState.CONNECTED, BlockConnectionState.BASED)
            .end();


        // simpleBlock(block, multipartBuilder);
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

        registerSignPoles();
    }
}