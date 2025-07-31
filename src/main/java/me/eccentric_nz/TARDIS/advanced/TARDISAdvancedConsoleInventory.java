package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.GlowstoneCircuit;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.HashMap;

public class TARDISAdvancedConsoleInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final String uuid;
    private final int id;
    private final Inventory inventory;

    public TARDISAdvancedConsoleInventory(TARDIS plugin, String uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
        this.inventory = plugin.getServer().createInventory(this, 18, Component.text("TARDIS Console", NamedTextColor.DARK_RED));
        this.inventory.setContents(getContents());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getContents() {
        ItemStack[] stacks;
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetDiskStorage storage = new ResultSetDiskStorage(plugin, where);
        if (storage.resultSet()) {
            String console = storage.getConsole();
            if (!console.isEmpty()) {
                try {
                    stacks = TARDISSerializeInventory.itemStacksFromString(console);
                    for (ItemStack circuit : stacks) {
                        if (circuit != null && circuit.hasItemMeta()) {
                            ItemMeta cm = circuit.getItemMeta();
                            if (circuit.getType().equals(Material.FILLED_MAP)) {
                                if (cm.hasDisplayName()) {
                                    GlowstoneCircuit glowstone = GlowstoneCircuit.getByName().get(ComponentUtils.stripColour(cm.displayName()));
                                    if (glowstone != null) {
                                        circuit.setType(Material.GLOWSTONE_DUST);
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    plugin.debug("Could not read console from database!");
                    stacks = new ItemStack[18];
                }
            } else {
                stacks = new ItemStack[18];
            }
        } else {
            stacks = create(id);
        }
        return stacks;
    }

    private ItemStack[] create(int id) {
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", uuid);
        set.put("tardis_id", id);
        // a non-empty console record is required for area storage
        set.put("console", "rO0ABXcEAAAAEnBwcHBwcHBwcHBwcHBwcHBwcA==");
        plugin.getQueryFactory().doInsert("storage", set);
        return new ItemStack[18];
    }
}
