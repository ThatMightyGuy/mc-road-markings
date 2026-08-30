package com.jetfly.roadmarkings;

import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;

import java.util.EnumMap;
import java.util.Map;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.world.level.block.Block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.VariantProperties.Rotation;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;

public class ModBlockStateProvider extends BlockStateProvider {

    protected static final String BLOCK_PATH = "block/";

    // Parameter values are provided by GatherDataEvent.
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
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
        ResourceLocation tex = modLoc("block/signpost");
        ResourceLocation baseTex = modLoc("block/signpost_base");

        BlockModelBuilder core = models().withExistingParent("sign_post_metal_core", modLoc(BLOCK_PATH + "sign_post_core"));
        core.texture("all", tex);
        core.texture("particle", tex);
        BlockModelBuilder nibble = models().withExistingParent("sign_post_metal_nibble", modLoc(BLOCK_PATH + "sign_post_nibble"));
        nibble.texture("all", tex);
        nibble.texture("particle", tex);
        BlockModelBuilder base = models().withExistingParent("sign_post_metal_base", modLoc(BLOCK_PATH + "sign_post_base"));
        base.texture("all", baseTex);
        base.texture("particle", baseTex);

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
            .end()

            .part()
                .modelFile(base)
                .rotationX(270)
                .addModel()
                .condition(SignPoleBlock.NORTH, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(base)
                .rotationX(90)
                .addModel()
                .condition(SignPoleBlock.SOUTH, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(base)
                .rotationX(90)
                .rotationY(270)
                .addModel()
                .condition(SignPoleBlock.EAST, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(base)
                .rotationX(90)
                .rotationY(90)
                .addModel()
                .condition(SignPoleBlock.WEST, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(base)
                .rotationX(180)
                .addModel()
                .condition(SignPoleBlock.UP, BlockConnectionState.BASED)
            .end()
            .part()
                .modelFile(base)
                .addModel()
                .condition(SignPoleBlock.DOWN, BlockConnectionState.BASED)
            .end();
    }

    private static Map<Direction, ModelRotation> getSignRotations() {
        Map<Direction, ModelRotation> rot = new EnumMap<>(Direction.class);
        rot.put(Direction.NORTH, new ModelRotation(0, 0));
        rot.put(Direction.UP,    new ModelRotation(90, 0));
        rot.put(Direction.DOWN,  new ModelRotation(270, 0));
        rot.put(Direction.EAST,  new ModelRotation(0, 90));
        rot.put(Direction.SOUTH, new ModelRotation(0, 180));
        rot.put(Direction.WEST,  new ModelRotation(0, 270));
        return rot;
    }
    private final Map<Direction, ModelRotation> signRotations = getSignRotations();

    private void registerSign(BlockModelBuilder model, BlockModelBuilder mount, SignShape shape, MultiPartBlockStateBuilder builder, String[] patterns) {
        for(Map.Entry<Direction, ModelRotation> rot : signRotations.entrySet())
        {
            Direction dir = rot.getKey();
            ModelRotation angle = rot.getValue();
            builder
                .part()
                    .modelFile(mount)
                    .rotationX(angle.x)
                    .rotationY(angle.y)
                    .addModel()
                    .condition(SignBlock.FACING, dir)
                .end()
                .part()
                    .modelFile(model)
                    .rotationX(angle.x)
                    .rotationY(angle.y)
                    .addModel()
                    .condition(SignBlock.FACING, dir)
                    .condition(SignBlock.SIGN_SHAPE, shape)
                .end();
        }
    }

    private void registerSigns() {
        ResourceLocation tex = modLoc("block/sign_1");
        ResourceLocation baseTex = modLoc("block/sign_back");

        BlockModelBuilder mount = models().withExistingParent("sign_mount_metal", modLoc(BLOCK_PATH + "sign_mount"));
        mount.texture("all", baseTex);
        mount.texture("particle", baseTex);

        BlockModelBuilder square = models().withExistingParent("sign_square_metal_base", modLoc(BLOCK_PATH + "sign_square_base"));
        square.texture("back", baseTex);
        square.texture("particle", baseTex);

        BlockModelBuilder circle = models().withExistingParent("sign_circle_metal_base", modLoc(BLOCK_PATH + "sign_circle_base"));
        circle.texture("back", baseTex);
        circle.texture("particle", baseTex);

        BlockModelBuilder octagon = models().withExistingParent("sign_octagon_metal_base", modLoc(BLOCK_PATH + "sign_octagon_base"));
        octagon.texture("back", baseTex);
        octagon.texture("particle", baseTex);

        BlockModelBuilder rhombus = models().withExistingParent("sign_rhombus_metal_base", modLoc(BLOCK_PATH + "sign_rhombus_base"));
        rhombus.texture("back", baseTex);
        rhombus.texture("particle", baseTex);

        BlockModelBuilder triangle = models().withExistingParent("sign_triangle_metal_base", modLoc(BLOCK_PATH + "sign_triangle_base"));
        triangle.texture("back", baseTex);
        triangle.texture("particle", baseTex);

        SignBlock block = RoadMarkings.SIGN_BLOCK.get();

        MultiPartBlockStateBuilder multipartBuilder = getMultipartBuilder(block);

        registerSign(square, mount, SignShape.SQUARE, multipartBuilder);
        registerSign(square, mount, SignShape.CIRCLE, multipartBuilder);
        registerSign(square, mount, SignShape.OCTAGON, multipartBuilder);
        registerSign(square, mount, SignShape.RHOMBUS, multipartBuilder);
        registerSign(square, mount, SignShape.TRIANGLE, multipartBuilder);

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