package me.eccentric_nz.TARDIS.info.dialog;

import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.HashMap;

public class ItemLookup {

    public static final HashMap<TARDISInfoMenu, InfoIcon> ITEMS = new HashMap<>();

    static {
        ITEMS.put(TARDISInfoMenu.KEY_INFO, new InfoIcon(Items.GOLD_NUGGET, "TARDIS Key", ResourceLocation.fromNamespaceAndPath("tardis", "brass_yale_key")));
        ITEMS.put(TARDISInfoMenu.SONIC_SCREWDRIVER_INFO, new InfoIcon(Items.BLAZE_ROD, "Sonic Screwdriver", ResourceLocation.fromNamespaceAndPath("tardis", "sonic_eleventh")));
        ITEMS.put(TARDISInfoMenu.LOCATOR_INFO, new InfoIcon(Items.COMPASS, "TARDIS Locator", ResourceLocation.fromNamespaceAndPath("tardis", "locator_16")));
        ITEMS.put(TARDISInfoMenu.STATTENHEIM_REMOTE_INFO, new InfoIcon(Items.FLINT, "Stattenheim Remote", ResourceLocation.fromNamespaceAndPath("tardis", "stattenheim_remote")));
        ITEMS.put(TARDISInfoMenu.BIOME_READER_INFO, new InfoIcon(Items.BRICK, "Biome Reader", ResourceLocation.fromNamespaceAndPath("tardis", "biome_reader")));
        ITEMS.put(TARDISInfoMenu.REMOTE_KEY_INFO, new InfoIcon(Items.OMINOUS_TRIAL_KEY, "TARDIS Remote Key", ResourceLocation.fromNamespaceAndPath("tardis", "remote_key")));
        ITEMS.put(TARDISInfoMenu.ARTRON_CAPACITOR_INFO, new InfoIcon(Items.BUCKET, "Artron Capacitor", ResourceLocation.fromNamespaceAndPath("tardis", "artron_capacitor")));
        ITEMS.put(TARDISInfoMenu.ARTRON_STORAGE_CELL_INFO, new InfoIcon(Items.BUCKET, "Artron Storage Cell", ResourceLocation.fromNamespaceAndPath("tardis", "artron_battery")));
        ITEMS.put(TARDISInfoMenu.ARTRON_FURNACE_INFO, new InfoIcon(Items.FURNACE, "Artron Furnace", ResourceLocation.fromNamespaceAndPath("tardis", "artron_furnace")));
        ITEMS.put(TARDISInfoMenu.PERCEPTION_FILTER_INFO, new InfoIcon(Items.OMINOUS_TRIAL_KEY, "Perception Filter", ResourceLocation.fromNamespaceAndPath("tardis", "perception_filter_string_key")));
        ITEMS.put(TARDISInfoMenu.SONIC_GENERATOR_INFO, new InfoIcon(Items.FLOWER_POT, "Sonic Generator", ResourceLocation.fromNamespaceAndPath("tardis", "sonic_generator")));
        ITEMS.put(TARDISInfoMenu.SONIC_BLASTER_INFO, new InfoIcon(Items.GOLDEN_HOE, "Sonic Blaster", ResourceLocation.fromNamespaceAndPath("tardis", "sonic_blaster")));
        ITEMS.put(TARDISInfoMenu.VORTEX_MANIPULATOR_INFO, new InfoIcon(Items.CLOCK, "Vortex Manipulator", ResourceLocation.fromNamespaceAndPath("tardis", "vortex_manipulator")));
        ITEMS.put(TARDISInfoMenu.HANDLES_INFO, new InfoIcon(Items.BIRCH_BUTTON, "", ResourceLocation.fromNamespaceAndPath("tardis", "handles_off")));
    }
}
