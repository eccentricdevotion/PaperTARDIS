/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.travel.save;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISSavesPlanetListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISSavesPlanetListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI,
     * then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onSavesPlanetClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!(event.getInventory().getHolder(false) instanceof TARDISSavesPlanetInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        // get the TARDIS the player is in
        int id = -1;
        if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(uuid)) {
            // junk mode
            id = plugin.getTrackerKeeper().getJunkPlayers().get(uuid);
        } else if (plugin.getTrackerKeeper().getSavesIds().containsKey(uuid)) {
            // player wants own saves
            id = plugin.getTrackerKeeper().getSavesIds().get(uuid);
        } else {
            // saves for the tardis the player is in
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
            if (rst.resultSet()) {
                id = rst.getTardis_id();
            }
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        event.setCancelled(true);
        if (slot == 0 || slot == 2) {
            // home location
            ItemStack is = view.getItem(slot);
            if (is == null) {
                return;
            }
            ItemMeta im = is.getItemMeta();
            List<Component> lore = im.lore();
            World w = TARDISAliasResolver.getWorldFromAlias(ComponentUtils.stripColour(lore.getFirst()));
            if (w == null) {
                close(player);
                return;
            }
            int x = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(1)));
            int y = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(2)));
            int z = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(3)));
            Location save_dest = new Location(w, x, y, z);
            // get tardis artron level
            ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
            if (!rs.fromID(id)) {
                close(player);
                return;
            }
            int level = rs.getArtronLevel();
            int travel = plugin.getArtronConfig().getInt("travel");
            if (level < travel) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                close(player);
                return;
            }
            Location exterior = null;
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (rsc.resultSet()) {
                exterior = rsc.getCurrent().location();
            }
            if (!save_dest.equals(exterior) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                HashMap<String, Object> set = new HashMap<>();
                set.put("world", ComponentUtils.stripColour(lore.getFirst()));
                set.put("x", TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(1))));
                set.put("y", TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(2))));
                set.put("z", TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(3))));
                int l_size = lore.size();
                if (l_size >= 5) {
                    String four = ComponentUtils.stripColour(lore.get(4));
                    if (!four.isEmpty() && !four.equals("Current location")) {
                        set.put("direction", four);
                    }
                    if (l_size > 5) {
                        String five = ComponentUtils.stripColour(lore.get(5));
                        if (five.equals("true")) {
                            set.put("submarine", 1);
                        } else {
                            set.put("submarine", 0);
                        }
                    }
                }
                if (l_size >= 7) {
                    String six = ComponentUtils.stripColour(lore.get(6));
                    if (!six.equals("Current location")) {
                        HashMap<String, Object> sett = new HashMap<>();
                        sett.put("chameleon_preset", six);
                        // set chameleon adaption to OFF
                        sett.put("adapti_on", 0);
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        plugin.getQueryFactory().doSyncUpdate("tardis", sett, wheret);
                    }
                }
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", id);
                plugin.getQueryFactory().doSyncUpdate("next", set, wheret);
                TravelType travelType = (ComponentUtils.stripColour(im.displayName()).equals("Home")) ? TravelType.HOME : TravelType.SAVE;
                plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, travelType));
                plugin.getTrackerKeeper().getRescue().remove(id);
                close(player);
                plugin.getMessenger().sendJoined(player, "DEST_SET_TERMINAL", ComponentUtils.stripColour(im.displayName()), !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                    new TARDISLand(plugin, id, player).exitVortex();
                    plugin.getPM().callEvent(new TARDISTravelEvent(player, null, travelType, id));
                }
            } else if (!lore.contains(Component.text("Current location", NamedTextColor.GOLD))) {
                lore.add(Component.text("Current location", NamedTextColor.GOLD));
                im.lore(lore);
                is.setItemMeta(im);
            }
        }
        if (slot >= 8 && slot < 45) {
            ItemStack is = view.getItem(slot);
            if (is != null) {
                ItemMeta im = is.getItemMeta();
                String alias = ComponentUtils.stripColour(im.displayName());
                String world = TARDISAliasResolver.getWorldNameFromAlias(alias);
                player.openInventory(new TARDISSavesInventory(plugin, id, world).getInventory());
            }
        }
    }
}
