package com.jetfly.roadmarkings;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nonnull;

public class SignPoleBlock extends BasePoleBlock {
    protected static final VoxelShape SHAPE_CORE  = Block.box(6, 6, 6, 10, 10, 10);
    protected static final VoxelShape SHAPE_NORTH = Block.box(6, 6, 0, 10, 10, 6);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(6, 6, 10, 10, 10, 16);
    protected static final VoxelShape SHAPE_EAST  = Block.box(10, 6, 6, 16, 10, 10);
    protected static final VoxelShape SHAPE_WEST  = Block.box(0, 6, 6, 6, 10, 10);
    protected static final VoxelShape SHAPE_UP    = Block.box(6, 10, 6, 10, 16, 10);
    protected static final VoxelShape SHAPE_DOWN  = Block.box(6, 0, 6, 10, 6, 10);

    public SignPoleBlock(Properties properties) {
        super(properties);
    }

    private static boolean hasNibble(BlockConnectionState state) {
        return state == BlockConnectionState.BASED || state == BlockConnectionState.CONNECTED;
    }

    @Override
    public VoxelShape getShape(
        @Nonnull BlockState state,
        @Nonnull BlockGetter level,
        @Nonnull BlockPos pos,
        @Nonnull CollisionContext context
    ) {
        VoxelShape shape = SHAPE_CORE;
        if (hasNibble(state.getValue(NORTH))) shape = Shapes.or(shape, SHAPE_NORTH);
        if (hasNibble(state.getValue(SOUTH))) shape = Shapes.or(shape, SHAPE_SOUTH);
        if (hasNibble(state.getValue(EAST)))  shape = Shapes.or(shape, SHAPE_EAST);
        if (hasNibble(state.getValue(WEST)))  shape = Shapes.or(shape, SHAPE_WEST);
        if (hasNibble(state.getValue(UP)))    shape = Shapes.or(shape, SHAPE_UP);
        if (hasNibble(state.getValue(DOWN)))  shape = Shapes.or(shape, SHAPE_DOWN);
        return shape;
    }
}
