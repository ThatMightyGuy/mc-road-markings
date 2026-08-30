package com.jetfly.roadmarkings;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

@ModBlock
public class SignBlock extends BaseSignBlock {
    public static final EnumProperty<SignShape> SIGN_SHAPE = EnumProperty.create("sign_shape", SignShape.class);
    public SignBlock(Properties properties) {
        super(properties);

        registerDefaultState(stateDefinition.any()
            .setValue(SIGN_SHAPE, SignShape.SQUARE)
        );
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(SIGN_SHAPE);
    }
}
