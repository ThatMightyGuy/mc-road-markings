package com.jetfly.roadmarkings;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import javax.annotation.Nonnull;

import com.mojang.serialization.MapCodec;

@ModBlock
public class BasePoleBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<BlockConnectionState> NORTH = EnumProperty.create("north", BlockConnectionState.class);
    public static final EnumProperty<BlockConnectionState> SOUTH = EnumProperty.create("south", BlockConnectionState.class);
    public static final EnumProperty<BlockConnectionState> EAST = EnumProperty.create("east", BlockConnectionState.class);
    public static final EnumProperty<BlockConnectionState> WEST = EnumProperty.create("west", BlockConnectionState.class);
    public static final EnumProperty<BlockConnectionState> UP = EnumProperty.create("up", BlockConnectionState.class);
    public static final EnumProperty<BlockConnectionState> DOWN = EnumProperty.create("down", BlockConnectionState.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BasePoleBlock(Properties properties) {
        super(properties);

        registerDefaultState(stateDefinition.any()
            .setValue(NORTH, BlockConnectionState.NONE)
            .setValue(EAST, BlockConnectionState.NONE)
            .setValue(SOUTH, BlockConnectionState.NONE)
            .setValue(WEST, BlockConnectionState.NONE)
            .setValue(UP, BlockConnectionState.NONE)
            .setValue(DOWN, BlockConnectionState.NONE)
        );
    }

    public static @Nonnull EnumProperty<BlockConnectionState> getConnectionProperty(Direction dir) {
        return switch (dir) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST  -> EAST;
            case WEST  -> WEST;
            case UP    -> UP;
            case DOWN  -> DOWN;
        };
    }

    public static @Nonnull BlockConnectionState getConnection(BlockState state, Direction dir) {
        switch(dir) {
            case NORTH:
                return state.getValue(NORTH);
            case SOUTH:
                return state.getValue(SOUTH);
            case EAST:
                return state.getValue(EAST);
            case WEST:
                return state.getValue(WEST);
            case UP:
                return state.getValue(UP);
            case DOWN:
                return state.getValue(DOWN);
            default:
                return BlockConnectionState.NONE;
        }
    }

    protected static @Nonnull BlockConnectionState getConnectionState(BlockGetter level, BlockPos pos, Direction dir) {
        BlockState neighbor = level.getBlockState(pos.relative(dir));
        Direction opposite = dir.getOpposite();

        if(neighbor.getBlock() instanceof BasePoleBlock) {
            if(getConnection(neighbor, opposite) == BlockConnectionState.DISCONNECTED)
                return BlockConnectionState.NONE;
            return BlockConnectionState.CONNECTED;
        }
        if(neighbor.isFaceSturdy(level, pos, opposite)) {
            return BlockConnectionState.BASED;
        }
        return BlockConnectionState.NONE;
    }

    @Override
    public MapCodec<BasePoleBlock> codec() {
        return simpleCodec(BasePoleBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, WATERLOGGED);
    }

    @Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext ctx) {
        BlockGetter level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        return super.getStateForPlacement(ctx)
            .setValue(NORTH, getConnectionState(level, pos, Direction.NORTH))
            .setValue(SOUTH, getConnectionState(level, pos, Direction.SOUTH))
            .setValue(EAST,  getConnectionState(level, pos, Direction.EAST))
            .setValue(WEST,  getConnectionState(level, pos, Direction.WEST))
            .setValue(UP,    getConnectionState(level, pos, Direction.UP))
            .setValue(DOWN,  getConnectionState(level, pos, Direction.DOWN))
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
        // Disconnected faces should stay disconnected
        if(getConnection(state, facing) == BlockConnectionState.DISCONNECTED)
            return state;
        BlockConnectionState connection = getConnectionState(level, currentPos, facing);
        return state.setValue(getConnectionProperty(facing), connection);
    }

    protected static Direction getHitDirection(BlockPos pos, BlockHitResult hit) {
        Vec3 localPos = hit.getLocation().subtract(Vec3.atCenterOf(pos));
        return Direction.getNearest(localPos);
    }

    protected static BlockConnectionState toggleConnection(BlockState state, LevelAccessor level, BlockPos pos, Direction dir) {
        BlockConnectionState connection = state.getValue(getConnectionProperty(dir));
        if(connection == BlockConnectionState.CONNECTED || connection == BlockConnectionState.BASED) {
            return BlockConnectionState.DISCONNECTED;
        }
        Direction opposite = dir.getOpposite();
        BlockPos neighborPos = pos.relative(dir);
        BlockState neighbor = level.getBlockState(neighborPos);

        // I *really* do not like that this method modifies the world,
        // but this was the cleanest solution I can think of
        if(neighbor.getBlock() instanceof BasePoleBlock) {
            if(neighbor.getValue(getConnectionProperty(opposite)) == BlockConnectionState.DISCONNECTED)
            {
                level.setBlock(neighborPos, neighbor.setValue(getConnectionProperty(opposite), BlockConnectionState.CONNECTED), 2);
                return BlockConnectionState.CONNECTED;
            }
        }
        return getConnectionState(level, pos, dir);
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
                Direction hitDirection = getHitDirection(pos, hitResult);
                BlockConnectionState newState = toggleConnection(state, level, pos, hitDirection);
                RoadMarkings.LOGGER.info("Toggling direction {}, new state is {}", hitDirection, newState);
                level.setBlock(pos, state.setValue(getConnectionProperty(hitDirection), newState), 3);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}