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
package me.eccentric_nz.TARDIS.playerprefs;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodels.GUIPlayerPreferences;
import me.eccentric_nz.TARDIS.custommodels.GUIWeather;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.enumeration.HADS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that planet.
 *
 * @author eccentric_nz
 */
public class TARDISGeneralPrefsInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Inventory inventory;

    public TARDISGeneralPrefsInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.inventory = plugin.getServer().createInventory(this, 36, Component.text("Player Prefs Menu", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {
        // get player preferences
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        List<Boolean> values = new ArrayList<>();
        if (!rsp.resultSet()) {
            // make a new record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("player_prefs", set);
            // get the new record
            rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            rsp.resultSet();
        }
        Player player = plugin.getServer().getPlayer(uuid);
        values.add(rsp.isAutoOn()); // 0
        values.add(rsp.isAutoSiegeOn()); // 1
        values.add(rsp.isAutoRescueOn()); // 2
        values.add(rsp.isBeaconOn()); // 3
        values.add(rsp.isCloseGUIOn()); // 4
        values.add(rsp.isDND()); // 5
        values.add(rsp.isDynamicLightsOn()); // 6
        values.add(rsp.isEpsOn()); // 7
        values.add(rsp.isHadsOn()); // 8
        values.add(rsp.getHadsType().equals(HADS.DISPERSAL)); // 9
        values.add(rsp.isQuotesOn()); // 10
        values.add(rsp.isRendererOn()); // 11
        values.add(rsp.isSfxOn()); // 12
        values.add(rsp.isSubmarineOn()); // 13
        values.add(rsp.isBuildOn()); // 14
        values.add(rsp.isSignOn()); // 15
        values.add(rsp.isTravelbarOn()); // 16
        values.add(rsp.isFarmOn()); // 17
        values.add(rsp.isTelepathyOn()); // 18
        // get TARDIS preset
        Tardis tardis = null;
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid.toString());
        ResultSetTardis rst = new ResultSetTardis(plugin, wherep, "", false);
        boolean hasTARDIS = rst.resultSet();
        if (hasTARDIS) {
            tardis = rst.getTardis();
            values.add(rst.getTardis().getPreset().equals(ChameleonPreset.JUNK_MODE)); // junk mode - 19
        } else {
            values.add(false); // 19
        }
        values.add(rsp.isAutoPowerUp()); // 20
        values.add(plugin.getTrackerKeeper().getActiveForceFields().containsKey(uuid)); // 21
        values.add(rsp.isMinecartOn()); // 22
        if (plugin.isWorldGuardOnServer()) {
            String chunk = tardis != null ? rst.getTardis().getChunk() : "TARDIS_TimeVortex:1";
            String[] split = chunk.split(":");
            World world = plugin.getServer().getWorld(split[0]);
            values.add(!plugin.getWorldGuardUtils().queryContainers(world, player.getName())); // lock containers - 23
        } else {
            values.add(false); // 23
        }
        values.add(rsp.isInfoOn()); // 24
        values.add(rsp.isAnnounceRepeatersOn()); // 25
        values.add(rsp.isOpenDisplayDoorOn()); // 26
        values.add(rsp.isDialogsOn()); // 27
        ItemStack[] stack = new ItemStack[36];
        for (GUIPlayerPreferences pref : GUIPlayerPreferences.values()) {
            if (pref.getMaterial() == Material.REPEATER) {
                ItemStack is = ItemStack.of(pref.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text(pref.getName()));
                boolean v;
                if (pref == GUIPlayerPreferences.JUNK_TARDIS) {
                    v = (tardis != null && tardis.getPreset().equals(ChameleonPreset.JUNK_MODE));
                } else {
                    v = values.get(pref.getSlot());
                }
                if (pref.getOffFloats() != null) {
                    CustomModelDataComponent component = im.getCustomModelDataComponent();
                    component.setFloats(v ? pref.getOnFloats() : pref.getOffFloats());
                    im.setCustomModelDataComponent(component);
                }
                if (pref == GUIPlayerPreferences.HADS_TYPE) {
                    im.lore(List.of(Component.text(v ? "DISPERSAL" : "DISPLACEMENT")));
                } else {
                    im.lore(List.of(
                            Component.text(v
                                    ? plugin.getLanguage().getString("SET_ON", "ON")
                                    : plugin.getLanguage().getString("SET_OFF", "OFF"))
                    ));
                }
                is.setItemMeta(im);
                stack[pref.getSlot()] = is;
            }
        }
        if (!plugin.isWorldGuardOnServer()) {
            stack[GUIPlayerPreferences.LOCK_CONTAINERS.getSlot()] = null;
        }
        // back
        ItemStack back = ItemStack.of(GUIChameleonPresets.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.displayName(Component.text("Back"));
        back.setItemMeta(but);
        stack[33] = back;
        // close
        ItemStack close = ItemStack.of(GUIWeather.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        stack[35] = close;
        return stack;
    }
}
