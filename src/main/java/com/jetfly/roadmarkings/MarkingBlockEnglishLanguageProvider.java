package com.jetfly.roadmarkings;

import java.util.Map;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

public class MarkingBlockEnglishLanguageProvider extends LanguageProvider {
    public MarkingBlockEnglishLanguageProvider(PackOutput output) {
        super(
            output,
            RoadMarkings.MODID,
            "en_us"
        );
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.roadmarkings", "Road Markings");

        ModBlocks blocks = RoadMarkings.MOD_BLOCKS;

        for(Map.Entry<String, DeferredBlock<Block>> kvp : blocks.paint.entrySet()) {
            String name = kvp.getKey();
            addBlock(kvp.getValue(), EnglishLocalizer.translate(name));
        }
        for(Map.Entry<String, DeferredBlock<LargeMarkingBlock>> kvp : blocks.large.entrySet()) {
            String name = kvp.getKey();
            addBlock(kvp.getValue(), EnglishLocalizer.translate(name));
        }
        for(Map.Entry<String, DeferredBlock<SmallMarkingBlock>> kvp : blocks.small.entrySet()) {
            String name = kvp.getKey();
            addBlock(kvp.getValue(), EnglishLocalizer.translate(name));
        }
    }
}