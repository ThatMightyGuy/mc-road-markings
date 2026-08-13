package com.jetfly.roadmarkings;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;

import java.util.Map;

import net.minecraft.data.PackOutput;

public class MarkingBlockItemProvider extends ItemModelProvider {
    public MarkingBlockItemProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RoadMarkings.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModBlocks blocks = RoadMarkings.MOD_BLOCKS;

        for(Map.Entry<String, DeferredBlock<Block>> kvp : blocks.paint.entrySet()) {
            String name = kvp.getKey();
            withExistingParent(RoadMarkings.MODID + ":" + name, modLoc("block/" + name));
        }

        for(Map.Entry<String, DeferredBlock<SmallMarkingBlock>> kvp : blocks.small.entrySet()) {
            String name = kvp.getKey();
            withExistingParent(RoadMarkings.MODID + ":" + name, modLoc("block/marking_small_" + name))
            .transforms()
                .transform(ItemDisplayContext.GUI)
                .translation(0, 5.5f, 0)
                .rotation(30, 45, 0)
                .scale(0.75f).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .translation(0, 7f, 0).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .translation(0, 7f, 0).end()
            .end();
        }

        for(Map.Entry<String, DeferredBlock<LargeMarkingBlock>> kvp : blocks.large.entrySet()) {
            String name = kvp.getKey();
            withExistingParent(RoadMarkings.MODID + ":" + name, modLoc("block/marking_large_" + name))
            .transforms()
                .transform(ItemDisplayContext.GUI)
                .translation(0, 2.5f, 0)
                .rotation(30, 45, 0)
                .scale(0.4f).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .scale(0.4f)
                .translation(0, 4f, 0).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .scale(0.4f)
                .translation(0, 4f, 0).end()
            .end();
        }
    }
}
