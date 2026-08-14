package com.jetfly.roadmarkings;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
public class BaseMarkingBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty SLABBED = CommonProperties.SLABBED;

    public BaseMarkingBlock(Properties properties) {
        super(properties);

        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(SLABBED, false)
        );
    }

    public static boolean shouldSlab(BlockGetter level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        return belowState.is(BlockTags.SLABS);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        // Only survive if the block below has a collider
        return !belowState.getCollisionShape(level, below).isEmpty();
    }

    @Override
    public MapCodec<BaseMarkingBlock> codec() {
        return simpleCodec(BaseMarkingBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, SLABBED);
    }

    @Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx)
            .setValue(FACING, ctx.getHorizontalDirection())
            .setValue(SLABBED, shouldSlab(ctx.getLevel(), ctx.getClickedPos()));
	}

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (!canSurvive(state, level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }
        state.setValue(SLABBED, shouldSlab(level, currentPos));
        return state;
    }
}