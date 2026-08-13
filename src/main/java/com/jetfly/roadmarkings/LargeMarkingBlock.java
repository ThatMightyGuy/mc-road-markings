package com.jetfly.roadmarkings;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class LargeMarkingBlock extends BaseMarkingBlock {
    protected static final VoxelShape SHAPE_FULL = Block.box(-16.0, 0.0, -16.0, 32.0, 1, 32.0);
    protected static final VoxelShape SHAPE_SLAB = Block.box(-16.0, -8.0, -16.0, 32.0, -7, 32.0);

    public static final BooleanProperty SLABBED = CommonProperties.SLABBED;

    public LargeMarkingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return shouldSlab(level, pos) ? SHAPE_SLAB : SHAPE_FULL;
    }
}