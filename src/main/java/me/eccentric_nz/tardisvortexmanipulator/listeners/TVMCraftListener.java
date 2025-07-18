/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TVMCraftListener implements Listener {

    private final TARDIS plugin;

    public TVMCraftListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraftManipulator(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        ItemStack is = recipe.getResult();
        if (is.getType().equals(Material.CLOCK) && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && ComponentUtils.endsWith(is.getItemMeta().displayName(), "Vortex Manipulator")) {
            Player player = (Player) event.getWhoClicked();
            String uuid = player.getUniqueId().toString();
            // check if they have a manipulator record
            TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid);
            if (!rs.resultSet()) {
                // make a record
                HashMap<String, Object> set = new HashMap<>();
                set.put("uuid", uuid);
                plugin.getQueryFactory().doInsert("manipulator", set);
            }
        }
    }
}
