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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISTemporalListener implements Listener {

    private final TARDIS plugin;
    private final List<String> notthese = List.of("Fob Watch", "Vortex Manipulator");

    public TARDISTemporalListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player p = event.getPlayer();
        ItemStack inhand = p.getInventory().getItemInMainHand();
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) && inhand.getType().equals(Material.CLOCK) && TARDISPermission.hasPermission(p, "tardis.temporal")) {
            if (inhand.hasItemMeta() && inhand.getItemMeta().hasDisplayName() && notthese.contains(inhand.getItemMeta().displayName())) {
                return;
            }
            p.resetPlayerTime();
            plugin.getTrackerKeeper().getSetTime().remove(p.getUniqueId());
            plugin.getMessenger().send(p, TardisModule.TARDIS, "TEMPORAL_RESET");
        }
    }
}
