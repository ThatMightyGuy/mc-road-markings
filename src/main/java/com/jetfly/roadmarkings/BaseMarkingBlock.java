package com.jetfly.roadmarkings;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;

import javax.annotation.Nonnull;

import com.mojang.serialization.MapCodec;

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
        state.setValue(SLABBED, shouldSlab(level, currentPos));
        return state;
    }

    /*
        You know, screw it. I give up, it's not an essential feature anyway.
        I may return to this later, I just couldn't be bothered with trying things to make block destruction work.
    */

    /// TODO: Fix marking disassembly with wrenches

    // // Technically a fallback I suppose? See below for info
    // @Override
    // public ItemInteractionResult useItemOn(
    //     @Nonnull ItemStack stack,
    //     @Nonnull BlockState state,
    //     @Nonnull Level level,
    //     @Nonnull BlockPos pos,
    //     @Nonnull Player player,
    //     @Nonnull InteractionHand hand,
    //     @Nonnull BlockHitResult hitResult
    // ) {
    //     // NeoForge says to not use this for item ability checks
    //     // I can't find a mod that implements wrenches in a way that would work with ItemAbilities
    //     if(stack.is(Tags.Items.TOOLS_WRENCH)) {
    //         if(!level.isClientSide) {
    //             if(player.isShiftKeyDown())
    //                 level.destroyBlock(pos, !player.isCreative());
    //             else
    //                 level.setBlock(pos, state.rotate(level, pos, Rotation.CLOCKWISE_90), 3);
    //         }
    //         return ItemInteractionResult.SUCCESS;
    //     }

    //     return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    // }


    // // I think this is right? Again, couldn't find a mod to check and I don't want to add my own wrench
    // // This system seems really neat; could yall please use it?
    // @Override
    // @Nullable
    // public BlockState getToolModifiedState(@Nonnull BlockState state,
    //     @Nonnull UseOnContext context,
    //     @Nonnull ItemAbility itemAbility,
    //     boolean simulate
    // ) {
    //     if(simulate) return null;
    //     if (itemAbility == RoadMarkings.WRENCH_ROTATE) {
    //         return state.rotate(context.getLevel(), context.getClickedPos(), Rotation.CLOCKWISE_90);
    //     }

    //     if (itemAbility == RoadMarkings.WRENCH_DISASSEMBLE) {
    //         return state;
    //     }

    //     return super.getToolModifiedState(state, context, itemAbility, simulate);
    // }
}