package com.jetfly.roadmarkings;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class SmallMarkingBlock extends BaseMarkingBlock {
    protected static final VoxelShape SHAPE_FULL = Block.box(0.0, 0.0, 0.0, 16.0, 1, 16.0);
    protected static final VoxelShape SHAPE_SLAB = Block.box(0.0, -8.0, 0.0, 16.0, -7, 16.0);

    public SmallMarkingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return shouldSlab(level, pos) ? SHAPE_SLAB : SHAPE_FULL;
    }
}