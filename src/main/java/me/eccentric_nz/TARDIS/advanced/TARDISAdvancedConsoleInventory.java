package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.GlowstoneCircuit;
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
        this.inventory = plugin.getServer().createInventory(this, 18, Component.text("TARDIS Console", NamedTextColor.RED));
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
        ResultSetDiskStorage rsds = new ResultSetDiskStorage(plugin, where);
        if (rsds.resultSet()) {
            String console = rsds.getConsole();
            if (!console.isEmpty()) {
                try {
                    stacks = TARDISSerializeInventory.itemStacksFromString(console);
                    for (ItemStack circuit : stacks) {
                        if (circuit != null && circuit.hasItemMeta()) {
                            ItemMeta cm = circuit.getItemMeta();
                            if (circuit.getType().equals(Material.FILLED_MAP)) {
                                if (cm.hasDisplayName()) {
                                    GlowstoneCircuit glowstone = GlowstoneCircuit.getByName().get(cm.getDisplayName());
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
        HashMap<String, Object> setstore = new HashMap<>();
        setstore.put("uuid", uuid.toString());
        setstore.put("tardis_id", id);
        plugin.getQueryFactory().doInsert("storage", setstore);
        return new ItemStack[18];
    }
}
