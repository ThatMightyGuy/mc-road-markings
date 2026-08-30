package com.jetfly.roadmarkings;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

public class WrenchInteractionHandler{

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
                if(!(block instanceof ModBlock))
                    return;
                level.destroyBlock(pos, true);
                event.setCanceled(true);
                if(held.isDamageableItem())
                    held.hurtAndBreak(1, player, player.getEquipmentSlotForItem(held));
            }
        
    }
}
