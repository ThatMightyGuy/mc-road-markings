package com.jetfly.roadmarkings;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;

import javax.annotation.Nonnull;

import com.mojang.serialization.MapCodec;

@ModBlock
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
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
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
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, SLABBED);
    }

    @Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx)
            .setValue(FACING, ctx.getHorizontalDirection())
            .setValue(SLABBED, shouldSlab(ctx.getLevel(), ctx.getClickedPos()));
	}

    @Override
    public void tick(@Nonnull BlockState state,
        @Nonnull ServerLevel level,
        @Nonnull BlockPos pos,
        @Nonnull RandomSource random
    ) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState updateShape(
        @Nonnull BlockState state,
        @Nonnull Direction facing,
        @Nonnull BlockState facingState,
        @Nonnull LevelAccessor level,
        @Nonnull BlockPos currentPos,
        @Nonnull BlockPos facingPos
    ) {
        if (!canSurvive(state, level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }
        return state.setValue(SLABBED, shouldSlab(level, currentPos));
    }

    @Override
    public ItemInteractionResult useItemOn(
        @Nonnull ItemStack stack,
        @Nonnull BlockState state,
        @Nonnull Level level,
        @Nonnull BlockPos pos,
        @Nonnull Player player,
        @Nonnull InteractionHand hand,
        @Nonnull BlockHitResult hitResult
    ) {
        if(WrenchInteractionHandler.isWrench(stack)) {
            if(!level.isClientSide) {
                level.setBlock(pos, state.rotate(level, pos, Rotation.CLOCKWISE_90), 3);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}