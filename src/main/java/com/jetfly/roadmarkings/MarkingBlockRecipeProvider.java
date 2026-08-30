package com.jetfly.roadmarkings;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.Map;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class MarkingBlockRecipeProvider extends RecipeProvider {

    public MarkingBlockRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private <T extends Block> void buildMarkingRecipes(RecipeOutput consumer, int craftAmount, String[] patterns, Map<String, DeferredBlock<T>> pool, Map<DyeColor, TagKey<Item>> itemTags) {
        String result = "";

        for (DyeColor color : DyeColor.values()) {
            TagKey<Item> asphaltTag = MarkingItemTagsProvider.ITEM_TAGS_MARKING_PAINT.get(color);
            TagKey<Item> itemTag = itemTags.get(color);

            for(String target : patterns)
            {
                // Build pattern recipes from asphalt
                Item marking = pool.get(target + "_" + color).asItem();

                SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(asphaltTag),
                        RecipeCategory.DECORATIONS,
                        marking,
                        craftAmount
                    )
                    .unlockedBy("has_painted_asphalt", has(asphaltTag))
                    .save(consumer)
                ;

                // Build exchange recipes from #marking_size/color
                result = "roadmarkings:exchange_" + target + "_" + color.getSerializedName();

                SingleItemRecipeBuilder.stonecutting( 
                        Ingredient.of(itemTag),
                        RecipeCategory.DECORATIONS,
                        marking,
                        1
                    )
                    .unlockedBy("has_painted_asphalt", has(asphaltTag))
                    .save(consumer, result)
                ;
            }
        }
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {

        for (DyeColor color : DyeColor.values()) {
            Item concretePowder = getConcretePowder(color);
            Item paintedAsphalt = getPaintedAsphalt(color);

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, paintedAsphalt, 8)
                .pattern("CCC")
                .pattern("CSC")
                .pattern("CCC")
                .define('C', concretePowder)
                .define('S', Items.CLAY_BALL)
                .unlockedBy("has_concrete_powder", has(concretePowder))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(RoadMarkings.MODID, "asphalt_" + color.getSerializedName())
            );
        }

        buildMarkingRecipes(consumer, 16, RoadMarkings.PATTERNS_SMALL, RoadMarkings.MOD_BLOCKS.small, MarkingItemTagsProvider.ITEM_TAGS_MARKING_SMALL);

        buildMarkingRecipes(consumer, 8, RoadMarkings.PATTERNS_LARGE, RoadMarkings.MOD_BLOCKS.large, MarkingItemTagsProvider.ITEM_TAGS_MARKING_LARGE);
    }


    private Item getConcretePowder(DyeColor color) {
        return switch (color) {
            case WHITE      -> Items.WHITE_CONCRETE_POWDER;
            case ORANGE     -> Items.ORANGE_CONCRETE_POWDER;
            case MAGENTA    -> Items.MAGENTA_CONCRETE_POWDER;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_CONCRETE_POWDER;
            case YELLOW     -> Items.YELLOW_CONCRETE_POWDER;
            case LIME       -> Items.LIME_CONCRETE_POWDER;
            case PINK       -> Items.PINK_CONCRETE_POWDER;
            case GRAY       -> Items.GRAY_CONCRETE_POWDER;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_CONCRETE_POWDER;
            case CYAN       -> Items.CYAN_CONCRETE_POWDER;
            case PURPLE     -> Items.PURPLE_CONCRETE_POWDER;
            case BLUE       -> Items.BLUE_CONCRETE_POWDER;
            case BROWN      -> Items.BROWN_CONCRETE_POWDER;
            case GREEN      -> Items.GREEN_CONCRETE_POWDER;
            case RED        -> Items.RED_CONCRETE_POWDER;
            case BLACK      -> Items.BLACK_CONCRETE_POWDER;
            default         -> Items.WHITE_CONCRETE_POWDER;
        };
    }

    private Item getPaintedAsphalt(DyeColor color) {
        return RoadMarkings.MOD_BLOCKS.paint.get("asphalt_" + color).asItem();
    }

}