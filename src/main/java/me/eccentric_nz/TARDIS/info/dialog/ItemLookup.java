package me.eccentric_nz.TARDIS.info.dialog;

import me.eccentric_nz.TARDIS.custommodels.keys.RoomVariant;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.HashMap;

public class ItemLookup {

    public static final HashMap<TARDISInfoMenu, InfoIcon> ITEMS = new HashMap<>();

    static {
        // TARDIS items
        ITEMS.put(TARDISInfoMenu.KEY_INFO, new InfoIcon(Items.GOLD_NUGGET, "TARDIS Key", ResourceLocation.fromNamespaceAndPath("tardis", "brass_yale_key")));
        ITEMS.put(TARDISInfoMenu.SONIC_SCREWDRIVER_INFO, new InfoIcon(Items.BLAZE_ROD, "Sonic Screwdriver", ResourceLocation.fromNamespaceAndPath("tardis", "sonic_eleventh")));
        ITEMS.put(TARDISInfoMenu.LOCATOR_INFO, new InfoIcon(Items.COMPASS, "TARDIS Locator", ResourceLocation.fromNamespaceAndPath("tardis", "locator_16")));
        ITEMS.put(TARDISInfoMenu.STATTENHEIM_REMOTE_INFO, new InfoIcon(Items.FLINT, "Stattenheim Remote", ResourceLocation.fromNamespaceAndPath("tardis", "stattenheim_remote")));
        ITEMS.put(TARDISInfoMenu.BIOME_READER_INFO, new InfoIcon(Items.BRICK, "TARDIS Biome Reader", ResourceLocation.fromNamespaceAndPath("tardis", "biome_reader")));
        ITEMS.put(TARDISInfoMenu.REMOTE_KEY_INFO, new InfoIcon(Items.OMINOUS_TRIAL_KEY, "TARDIS Remote Key", ResourceLocation.fromNamespaceAndPath("tardis", "remote_key")));
        ITEMS.put(TARDISInfoMenu.ARTRON_CAPACITOR_INFO, new InfoIcon(Items.BUCKET, "Artron Capacitor", ResourceLocation.fromNamespaceAndPath("tardis", "artron_capacitor")));
        ITEMS.put(TARDISInfoMenu.ARTRON_STORAGE_CELL_INFO, new InfoIcon(Items.BUCKET, "Artron Storage Cell", ResourceLocation.fromNamespaceAndPath("tardis", "artron_battery")));
        ITEMS.put(TARDISInfoMenu.ARTRON_FURNACE_INFO, new InfoIcon(Items.FURNACE, "Artron Furnace", ResourceLocation.fromNamespaceAndPath("tardis", "artron_furnace")));
        ITEMS.put(TARDISInfoMenu.PERCEPTION_FILTER_INFO, new InfoIcon(Items.OMINOUS_TRIAL_KEY, "Perception Filter", ResourceLocation.fromNamespaceAndPath("tardis", "perception_filter_string_key")));
        ITEMS.put(TARDISInfoMenu.SONIC_GENERATOR_INFO, new InfoIcon(Items.FLOWER_POT, "Sonic Generator", ResourceLocation.fromNamespaceAndPath("tardis", "sonic_generator")));
        ITEMS.put(TARDISInfoMenu.SONIC_BLASTER_INFO, new InfoIcon(Items.GOLDEN_HOE, "Sonic Blaster", ResourceLocation.fromNamespaceAndPath("tardis", "sonic_blaster")));
        ITEMS.put(TARDISInfoMenu.VORTEX_MANIPULATOR_INFO, new InfoIcon(Items.CLOCK, "Vortex Manipulator", ResourceLocation.fromNamespaceAndPath("tardis", "vortex_manipulator")));
        ITEMS.put(TARDISInfoMenu.HANDLES_INFO, new InfoIcon(Items.BIRCH_BUTTON, "Handles", ResourceLocation.fromNamespaceAndPath("tardis", "handles_off")));
        // rooms
        ITEMS.put(TARDISInfoMenu.ALLAY, new InfoIcon(Items.LIGHT_BLUE_CONCRETE, "Allay House", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.ALLAY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.ANTIGRAVITY, new InfoIcon(Items.SANDSTONE, "Anti-gravity Well", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.ANTIGRAVITY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.APIARY, new InfoIcon(Items.BEE_NEST, "Apiary", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.APIARY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.AQUARIUM, new InfoIcon(Items.TUBE_CORAL_BLOCK, "Aquarium", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.AQUARIUM.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.ARBORETUM, new InfoIcon(Items.OAK_LEAVES, "Arboretum", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.ARBORETUM.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.BAKER, new InfoIcon(Items.END_STONE, "4th Doctor's Secondary Console", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.BAKER.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.BAMBOO, new InfoIcon(Items.BAMBOO, "Bamboo", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.BAMBOO.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.BEDROOM, new InfoIcon(Items.GLOWSTONE, "Bedroom", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.BEDROOM.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.BIRDCAGE, new InfoIcon(Items.YELLOW_GLAZED_TERRACOTTA, "Bird Cage", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.BIRDCAGE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.CHEMISTRY, new InfoIcon(Items.BLAST_FURNACE, "Chemistry Lab", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.CHEMISTRY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.EMPTY, new InfoIcon(Items.GLASS, "Empty", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.EMPTY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.EYE, new InfoIcon(Items.SHROOMLIGHT, "Eye of Harmony", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.EYE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.FARM, new InfoIcon(Items.DIRT, "Mob Farm", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.FARM.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.GARDEN, new InfoIcon(Items.CHERRY_LEAVES, "Flower Garden", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.GARDEN.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.GEODE, new InfoIcon(Items.AMETHYST_BLOCK, "Geode", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.GEODE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.GRAVITY, new InfoIcon(Items.MOSSY_COBBLESTONE, "Gravity Well", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.GRAVITY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.GREENHOUSE, new InfoIcon(Items.MELON, "Greenhouse", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.GREENHOUSE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.HARMONY, new InfoIcon(Items.STONE_BRICK_STAIRS, "Condenser", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.HARMONY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.HUTCH, new InfoIcon(Items.ACACIA_LOG, "Rabbit Hutch", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.HUTCH.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.IGLOO, new InfoIcon(Items.PACKED_ICE, "Igloo", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.IGLOO.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.IISTUBIL, new InfoIcon(Items.WHITE_GLAZED_TERRACOTTA, "Camel Stable", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.IISTUBIL.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.KITCHEN, new InfoIcon(Items.PUMPKIN, "Kitchen", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.KITCHEN.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.LAVA, new InfoIcon(Items.MAGMA_BLOCK, "Lava", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.LAVA.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.LAZARUS, new InfoIcon(Items.FURNACE, "Genetic Manipulator", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.LAZARUS.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.LIBRARY, new InfoIcon(Items.ENCHANTING_TABLE, "Library", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.LIBRARY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.MANGROVE, new InfoIcon(Items.MUDDY_MANGROVE_ROOTS, "Mangrove", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.MANGROVE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.MAZE, new InfoIcon(Items.LODESTONE, "Maze", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.MAZE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.MUSHROOM, new InfoIcon(Items.GRAVEL, "Mycellium", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.MUSHROOM.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.NETHER, new InfoIcon(Items.BLACKSTONE, "Nether", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.NETHER.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.OBSERVATORY, new InfoIcon(Items.POLISHED_BLACKSTONE_BRICKS, "Astronomical Observatory", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.OBSERVATORY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.PASSAGE, new InfoIcon(Items.CLAY, "Passage", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.PASSAGE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.PEN, new InfoIcon(Items.MOSS_BLOCK, "Sniffer Pen", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.PEN.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.POOL, new InfoIcon(Items.SNOW_BLOCK, "Pool", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.POOL.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.RAIL, new InfoIcon(Items.HOPPER, "Rail Transfer Station", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.RAIL.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.RENDERER, new InfoIcon(Items.TERRACOTTA, "Exterior Renderer", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.RENDERER.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.SHELL, new InfoIcon(Items.DEAD_BRAIN_CORAL_BLOCK, "Shell", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.SHELL.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.SMELTER, new InfoIcon(Items.CHEST, "Smelter", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.SMELTER.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.STABLE, new InfoIcon(Items.HAY_BLOCK, "Horse Stable", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.STABLE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.STALL, new InfoIcon(Items.BROWN_GLAZED_TERRACOTTA, "Llama Stall", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.STALL.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.SURGERY, new InfoIcon(Items.RED_CONCRETE, "Hospital Surgery", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.SURGERY.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.TRENZALORE, new InfoIcon(Items.BRICKS, "Trenzalore", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.TRENZALORE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.VAULT, new InfoIcon(Items.DISPENSER, "Storage Vault", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.VAULT.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.VILLAGE, new InfoIcon(Items.OAK_LOG, "Village", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.VILLAGE.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.WOOD, new InfoIcon(Items.OAK_PLANKS, "Wood Secondary Console", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.WOOD.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.WORKSHOP, new InfoIcon(Items.RED_NETHER_BRICKS, "Workshop", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.WORKSHOP.getKey().getKey())));
        ITEMS.put(TARDISInfoMenu.ZERO, new InfoIcon(Items.GRASS_BLOCK, "Zero Room", ResourceLocation.fromNamespaceAndPath("tardis", RoomVariant.ZERO.getKey().getKey())));
    }
}
