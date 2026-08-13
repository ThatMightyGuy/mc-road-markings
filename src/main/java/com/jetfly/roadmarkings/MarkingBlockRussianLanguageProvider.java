package com.jetfly.roadmarkings;

import java.util.Map;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

public class MarkingBlockRussianLanguageProvider extends LanguageProvider {
    public MarkingBlockRussianLanguageProvider(PackOutput output) {
        super(
            output,
            RoadMarkings.MODID,
            "ru_ru"
        );
    }

    @Override
    protected void addTranslations() {
        // Creative tab localization
        add("itemGroup.roadmarkings", "Дорожная разметка");

        ModBlocks blocks = RoadMarkings.MOD_BLOCKS;

        for(Map.Entry<String, DeferredBlock<Block>> kvp : blocks.paint.entrySet()) {
            String name = kvp.getKey();
            String localized = RussianLocalizer.translate(name);
            addBlock(kvp.getValue(), localized);
        }

        for(Map.Entry<String, DeferredBlock<SmallMarkingBlock>> kvp : blocks.small.entrySet()) {
            String name = kvp.getKey();
            String localized = RussianLocalizer.translate(name);
            addBlock(kvp.getValue(), localized);
        }

        for(Map.Entry<String, DeferredBlock<LargeMarkingBlock>> kvp : blocks.large.entrySet()) {
            String name = kvp.getKey();
            String localized = RussianLocalizer.translate(name);
            addBlock(kvp.getValue(), localized);
        }
    }
}