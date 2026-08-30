package com.jetfly.roadmarkings;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import java.util.List;

public class WrenchInteractionHandler {
    private WrenchInteractionHandler() {}

    public static boolean isWrench(ItemStack stack) {
        if(stack.canPerformAction(RoadMarkings.WRENCH_DISASSEMBLE) ||
            stack.canPerformAction(RoadMarkings.WRENCH_ROTATE))
            return true;

        // NeoForge says this is not the proper way to check, but no mod seems to implement ItemActions
        return stack.is(Tags.Items.TOOLS_WRENCH);
    }

    @SubscribeEvent
    public static void onRightClickBlock(RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;

        Player player = event.getEntity();
        ItemStack held = event.getItemStack();

        if(player.isShiftKeyDown() && isWrench(held)) {
            BlockPos pos = event.getPos();
            Block block = level.getBlockState(pos).getBlock();
            if(!(block instanceof ModBlock)) {
                return;
            }

            List<ItemStack> drops = Block.getDrops(
                level.getBlockState(pos),
                (ServerLevel) level,
                pos,
                level.getBlockEntity(pos),
                player,
                held
            );

            level.destroyBlock(pos, false);

            // We want to put the items in the player's inventory and drop them if that's not possible
            for(ItemStack stack : drops) {
                if(!player.isCreative() && !player.addItem(stack)) {
                    // Drop if the block can't fit
                    ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                    level.addFreshEntity(entity);
                }
            }

            event.setCanceled(true);
            if(held.isDamageableItem())
                held.hurtAndBreak(1, player, player.getEquipmentSlotForItem(held));
        }
    }
}