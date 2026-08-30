package com.jetfly.roadmarkings;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import javax.annotation.Nonnull;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import com.mojang.serialization.MapCodec;

@ModBlock
public class BaseSignBlock extends DirectionalBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty ROTATION = DirectionProperty.create("rotation", Direction.Plane.HORIZONTAL);

    public BaseSignBlock(Properties properties) {
        super(properties);

        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(ROTATION, Direction.NORTH)
            .setValue(WATERLOGGED, false)
        );
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
        BlockPos parent = pos.relative(state.getValue(FACING).getOpposite());
        BlockState parentState = level.getBlockState(parent);
        // Only survive if the block below has a collider
        return !parentState.getCollisionShape(level, parent).isEmpty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return state.getVisualShape(level, pos, ctx);
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
    public MapCodec<BaseSignBlock> codec() {
        return simpleCodec(BaseSignBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, ROTATION, WATERLOGGED);
    }

    @Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext ctx) {
        BlockGetter level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        return super.getStateForPlacement(ctx)
            .setValue(FACING,  ctx.getClickedFace())
            .setValue(ROTATION, Direction.NORTH)
            .setValue(WATERLOGGED, !level.getFluidState(pos).isEmpty());
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
        return state;
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
                Direction rotation = state.getValue(ROTATION);
                level.setBlock(pos, state.setValue(ROTATION, rotation.getClockWise()), 2);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}