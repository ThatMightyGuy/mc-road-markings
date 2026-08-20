package com.jetfly.roadmarkings;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.references.Items;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.TagKey;

import java.util.EnumMap;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

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

    public static final ItemAbility WRENCH_ROTATE = ItemAbility.get("c:wrench_rotate");
    public static final ItemAbility WRENCH_DISASSEMBLE = ItemAbility.get("c:wrench_disassemble");

    public static final @Nonnull String[] PATTERNS_LARGE = {
        "all_turns",
        "left",
        "left_right",
        "pig_path",
        "railroad_crossing",
        "right",
        "through",
        "through_left",
        "through_right",
        "stop",
        "ru_stop",
        "bus",
        "crossing_ahead",
        "kana_ni",
        "kanji_kei",
        "kanji_ryo",
        "kanji_sha",
        "kanji_wa",
        "lane",
        "priority_ahead",
        "diagonal",
        "diagonal_thin",
        "roundabout_lhd",
        "roundabout_rhd"
    };

    public static final @Nonnull String[] PATTERNS_SMALL = {
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
        "solid_t_shoulder_right",
        "hatch_diagonal_regular",
        "hatch_diagonal_tight",
        "hatch_diamond_regular",
        "hatch_diamond_tight",
        "dot",
        "jp_no_parking_straight",
        "ped_direction",
        "stop_line",
        "flush_line",
        "flush_line_thin",
        "line_crossing_ahead_zig",
        "line_crossing_ahead_zag",
        "zigzag_end_full",
        "zigzag_end_left",
        "zigzag_end_right",
    };

    private static final BlockBehaviour.Properties PROPERTIES_MARKINGS = BlockBehaviour.Properties.of()
        .strength(0.5f, 6.0f)
        .sound(SoundType.STONE)
        .noOcclusion()
        .noCollission()
        .isViewBlocking((state, level, pos) -> false)
        .isSuffocating((state, level, pos) -> false)
        .isRedstoneConductor((state, level, pos) -> false)
        .isValidSpawn((state, level, pos, type) -> true);

    private static final BlockBehaviour.Properties PROPERTIES_ASPHALT = BlockBehaviour.Properties.of()
        .strength(1.0f, 6.0F)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
        .isViewBlocking((state, level, pos) -> true)
        .isSuffocating((state, level, pos) -> true)
        .isRedstoneConductor((state, level, pos) -> true)
        .isValidSpawn((state, level, pos, type) -> true);

    private static final BlockBehaviour.Properties PROPERTIES_SIGN_POLES = BlockBehaviour.Properties.of()
        .strength(0.5f, 6.0F)
        .requiresCorrectToolForDrops()
        .sound(SoundType.METAL)
        .isViewBlocking((state, level, pos) -> false)
        .isSuffocating((state, level, pos) -> false)
        .isRedstoneConductor((state, level, pos) -> false)
        .isValidSpawn((state, level, pos, type) -> false);

    public static final DeferredBlock<SignPoleBlock> SIGN_POLE_BLOCK = BLOCKS.registerBlock("sign_pole", SignPoleBlock::new, PROPERTIES_SIGN_POLES);
    public static final DeferredItem<BlockItem> SIGN_POLE_BLOCK_ITEM = ITEMS.register("sign_pole", () -> new BlockItem(SIGN_POLE_BLOCK.get(), new Item.Properties()));

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

    private static <T extends Block> void addCategoryToTab(Map<String, DeferredBlock<T>> pool, String[] patterns, CreativeModeTab.Output output) {
        for(DyeColor color : DyeColor.values()) {
            for(String pattern : patterns) {
                DeferredBlock<T> block = pool.get(pattern + "_" + color.getSerializedName());
                output.accept(block);
            }
        }
    }

    private static void addToTab(ItemDisplayParameters params, CreativeModeTab.Output output) {
        addCategoryToTab(MOD_BLOCKS.paint, new String[] {"asphalt"}, output);
        addCategoryToTab(MOD_BLOCKS.large, PATTERNS_LARGE, output);
        addCategoryToTab(MOD_BLOCKS.small, PATTERNS_SMALL, output);
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MARKINGS_TAB = CREATIVE_MODE_TABS.register("road_markings", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.roadmarkings"))
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
