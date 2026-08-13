package com.jetfly.roadmarkings;

import org.slf4j.Logger;

import com.ibm.icu.util.CodePointTrie.Small;
import com.mojang.logging.LogUtils;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.TagKey;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.Map;
import net.minecraft.world.level.block.SoundType;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RoadMarkings.MODID)
public class RoadMarkings {
    public static final String MODID = "roadmarkings";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final String[] PATTERNS_LARGE = {
        "all_turns",
        "left",
        "left_right",
        "pig_path",
        "railroad_crossing",
        "right",
        "through",
        "through_left",
        "through_right",
        "stop"
    };

    public static final String[] PATTERNS_SMALL = {
        "bands_half",
        "bands_quarter",
        "bands_eighth",
        "double_corner",
        "double_cross",
        "double_straight",
        "double_t",
        "shoulder_double_inner",
        "shoulder_double_outer",
        "shoulder_double_straight",
        "shoulder_inner",
        "shoulder_outer",
        "shoulder_straight",
        "shoulder_t_left",
        "shoulder_t_right",
        "solid_corner",
        "solid_cross",
        "solid_cross_shoulder",
        "solid_straight",
        "solid_t",
        "solid_t_shoulder",
        "solid_t_shoulder_left",
        "solid_t_shoulder_right"
    };

    private static Map<DyeColor, TagKey<Block>> generateColorTags(String baseTag) {
        Map<DyeColor, TagKey<Block>> tags = new EnumMap<>(DyeColor.class);

        for(DyeColor color : DyeColor.values()) {
            TagKey<Block> tag = TagKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(MODID, baseTag + "/" + color.getSerializedName())
            );

            tags.put(color, tag);
        }

        return tags;
    }

    private static Map<DyeColor, TagKey<Item>> generateItemTags(Map<DyeColor, TagKey<Block>> tags) {
        Map<DyeColor, TagKey<Item>> result = new EnumMap<>(DyeColor.class);
        for(Map.Entry<DyeColor, TagKey<Block>> tag : tags.entrySet()) {
            result.put(tag.getKey(), TagKey.create(Registries.ITEM, tag.getValue().location()));
        }
        return result;
    }

    public static final Map<DyeColor, TagKey<Block>> TAGS_MARKING_LARGE = generateColorTags("marking_large");
    public static final Map<DyeColor, TagKey<Block>> TAGS_MARKING_SMALL = generateColorTags("marking_small");
    public static final Map<DyeColor, TagKey<Block>> TAGS_MARKING_PAINT = generateColorTags("asphalt");

    public static final Map<DyeColor, TagKey<Item>> ITEM_TAGS_MARKING_LARGE = generateItemTags(TAGS_MARKING_LARGE);
    public static final Map<DyeColor, TagKey<Item>> ITEM_TAGS_MARKING_SMALL = generateItemTags(TAGS_MARKING_SMALL);
    public static final Map<DyeColor, TagKey<Item>> ITEM_TAGS_MARKING_PAINT = generateItemTags(TAGS_MARKING_PAINT);

    private static final BlockBehaviour.Properties PROPERTIES_MARKINGS = BlockBehaviour.Properties.of()
        .strength(0.3F)
        .sound(SoundType.STONE)
        .noOcclusion()
        .noCollission()
        .isViewBlocking((state, level, pos) -> false)
        .isSuffocating((state, level, pos) -> false)
        .isRedstoneConductor((state, level, pos) -> false)
        .isValidSpawn((state, level, pos, type) -> true);

    private static final BlockBehaviour.Properties PROPERTIES_ASPHALT = BlockBehaviour.Properties.of()
        .strength(6.0F)
        .sound(SoundType.STONE)
        .isViewBlocking((state, level, pos) -> true)
        .isSuffocating((state, level, pos) -> true)
        .isRedstoneConductor((state, level, pos) -> true)
        .isValidSpawn((state, level, pos, type) -> true);

    private static ModBlocks registerBlocks() {
        ModBlocks blocks = new ModBlocks();

        for (DyeColor color : DyeColor.values()) {
            String col = color.getName();

            // Large blocks
            for (String pattern : PATTERNS_LARGE) {
                String name = pattern + "_" + col;
                DeferredBlock<LargeMarkingBlock> block = BLOCKS.registerBlock(name, LargeMarkingBlock::new, PROPERTIES_MARKINGS);
                blocks.large.put(name, block);
                ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
            }

            // Small blocks
            for (String pattern : PATTERNS_SMALL) {
                String name = pattern + "_" + col;
                DeferredBlock<SmallMarkingBlock> block = BLOCKS.registerBlock(name, SmallMarkingBlock::new, PROPERTIES_MARKINGS);
                blocks.small.put(name, block);
                ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
            }

            // Painted blocks
            String name = "asphalt_" + col;
            DeferredBlock<Block> block = BLOCKS.registerBlock(name, Block::new, PROPERTIES_ASPHALT);
            blocks.paint.put(name, block);
            ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        }
        return blocks;
    }

    public static final ModBlocks MOD_BLOCKS = registerBlocks();

    private static void addToTab(ItemDisplayParameters params, CreativeModeTab.Output output) {

        for(Map.Entry<String, DeferredBlock<Block>> entry : MOD_BLOCKS.paint.entrySet()) {
            output.accept(entry.getValue());
        }

        for(Map.Entry<String, DeferredBlock<SmallMarkingBlock>> entry : MOD_BLOCKS.small.entrySet()) {
            output.accept(entry.getValue());
        }

        for(Map.Entry<String, DeferredBlock<LargeMarkingBlock>> entry : MOD_BLOCKS.large.entrySet()) {
            output.accept(entry.getValue());
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("road_markings", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.roadmarkings")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(MOD_BLOCKS.small.get("double_straight_orange")))
            .displayItems(RoadMarkings::addToTab).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public RoadMarkings(IEventBus modEventBus, ModContainer modContainer) {

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (RoadMarkings) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(this::gatherData);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();


        generator.addProvider(
            event.includeClient(),
            new MarkingBlockStateProvider(output, existingFileHelper)
        );

        generator.addProvider(
            event.includeClient(),
            new MarkingBlockItemProvider(output, existingFileHelper)
        );

        MarkingBlockTagsProvider blockTagsProvider = new MarkingBlockTagsProvider(output, lookupProvider, existingFileHelper);

        generator.addProvider(
            event.includeServer(),
            blockTagsProvider
        );

        generator.addProvider(
            event.includeServer(),
            new MarkingItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper)
        );

        generator.addProvider(
            event.includeServer(),
            new ModLootTableProvider(output, lookupProvider)
        );

        generator.addProvider(
            event.includeServer(),
            new MarkingBlockRecipeProvider(output, lookupProvider)
        );

        generator.addProvider(
            event.includeClient(),
            new MarkingBlockEnglishLanguageProvider(output)
        );

        generator.addProvider(
            event.includeClient(),
            new MarkingBlockRussianLanguageProvider(output)
        );
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
