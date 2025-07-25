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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicItem;
import me.eccentric_nz.TARDIS.database.data.Sonic;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSonic;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISSonicGeneratorListener implements Listener {

    private final TARDIS plugin;

    public TARDISSonicGeneratorListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (!block.getType().equals(Material.FLOWER_POT)) {
            return;
        }
        String location = block.getLocation().toString();
        // get tardis from saved location
        HashMap<String, Object> where = new HashMap<>();
        where.put("type", 24);
        where.put("location", location);
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (rsc.resultSet() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            // check if the generator is activated
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("uuid", uuid.toString());
            ResultSetSonic rss = new ResultSetSonic(plugin, wheres);
            if (rss.resultSet()) {
                Sonic s = rss.getSonic();
                if (s.isActivated()) {
                    if (player.isSneaking()) {
                        // generate sonic
                        generate(player, block.getLocation(), s);
                    } else {
                        plugin.getTrackerKeeper().getSonicGenerators().put(uuid, block.getLocation());
                        // open GUI
                        player.openInventory(new TARDISSonicGeneratorInventory(plugin, s).getInventory());
                    }
                } else {
                    openActivate(player);
                }
            } else {
                openActivate(player);
            }
        }
    }

    private void generate(Player p, Location location, Sonic s) {
        // check they have enough Artron energy
        ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
        if (rs.fromUUID(p.getUniqueId().toString())) {
            double full = plugin.getArtronConfig().getDouble("full_charge") / 100.0d;
            int cost = (int) (plugin.getArtronConfig().getDouble("sonic_generator.standard") * full);
            int level = rs.getArtronLevel();
            ItemStack sonic = ItemStack.of(Material.BLAZE_ROD, 1);
            ItemMeta screw = sonic.getItemMeta();
            screw.displayName(ComponentUtils.toWhite("Sonic Screwdriver"));
            List<Component> upgrades = new ArrayList<>();
            if (s.hasKnockback()) {
                upgrades.add(Component.text("Knockback Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.knockback") * full);
            }
            if (s.hasBio()) {
                upgrades.add(Component.text("Bio-scanner Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full);
            }
            if (s.hasDiamond()) {
                upgrades.add(Component.text("Diamond Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.diamond") * full);
            }
            if (s.hasEmerald()) {
                upgrades.add(Component.text("Emerald Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.emerald") * full);
            }
            if (s.hasRedstone()) {
                upgrades.add(Component.text("Redstone Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full);
            }
            if (s.hasPainter()) {
                upgrades.add(Component.text("Painter Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.painter") * full);
            }
            if (s.hasIgnite()) {
                upgrades.add(Component.text("Ignite Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.ignite") * full);
            }
            if (s.hasArrow()) {
                upgrades.add(Component.text("Pickup Arrows Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.arrow") * full);
            }
            if (s.hasBrush()) {
                upgrades.add(Component.text("Brush Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.brush") * full);
            }
            if (s.hasConversion()) {
                upgrades.add(Component.text("Conversion Upgrade"));
                cost += (int) (plugin.getArtronConfig().getDouble("sonic_generator.conversion") * full);
            }
            if (!upgrades.isEmpty()) {
                List<Component> finalUps = new ArrayList<>();
                finalUps.add(Component.text("Upgrades:"));
                finalUps.addAll(upgrades);
                screw.lore(finalUps);
            }
            // set custom model data
            CustomModelDataComponent component = screw.getCustomModelDataComponent();
            component.setFloats(s.getModel());
            screw.setCustomModelDataComponent(component);
            sonic.setItemMeta(screw);
            if (cost < level) {
                Location loc = location.clone().add(0.5d, 0.75d, 0.5d);
                Entity drop = location.getWorld().dropItem(loc, sonic);
                drop.setVelocity(new Vector(0, 0, 0));
                plugin.getTrackerKeeper().getSonicGenerators().remove(p.getUniqueId());
                // remove the Artron energy
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", p.getUniqueId().toString());
                plugin.getQueryFactory().alterEnergyLevel("tardis", -cost, where, p);
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "UPGRADE_ABORT_ENERGY");
            }
        }
    }

    private void openActivate(Player p) {
        p.openInventory(new TARDISSonicActivatorInventory(plugin).getInventory());
    }

    @EventHandler(ignoreCancelled = true)
    public void onSonicGeneratorBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (!b.getType().equals(Material.FLOWER_POT)) {
            return;
        }
        // check location
        HashMap<String, Object> where = new HashMap<>();
        where.put("type", 24);
        where.put("location", b.getLocation().toString());
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (!rsc.resultSet()) {
            return;
        }
        // check if activated
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", event.getPlayer().getUniqueId().toString());
        ResultSetSonic rss = new ResultSetSonic(plugin, wheres);
        if (rss.resultSet() && rss.getSonic().isActivated()) {
            event.setCancelled(true);
            // remove Item Display
            TARDISDisplayItemUtils.remove(b);
            // set block to AIR
            b.setBlockData(TARDISConstants.AIR);
            // drop a custom FLOWER_POT_ITEM
            ItemStack is = ItemStack.of(Material.FLOWER_POT, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text("Sonic Generator"));
            im.setItemModel(SonicItem.SONIC_GENERATOR.getKey());
            is.setItemMeta(im);
            b.getWorld().dropItemNaturally(b.getLocation(), is);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSonicGeneratorPlace(BlockPlaceEvent event) {
        ItemStack is = event.getItemInHand();
        if (!is.getType().equals(Material.FLOWER_POT) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (im.hasDisplayName() && ComponentUtils.endsWith(im.displayName(), "Sonic Generator")) {
            Player p = event.getPlayer();
            String uuid = p.getUniqueId().toString();
            Block block = event.getBlock();
            TARDISDisplayItemUtils.set(TARDISDisplayItem.SONIC_GENERATOR, block, -1);
            String l = block.getLocation().toString();
            // generator was crafted
            // get tardis player is in
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid);
            ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
            if (rs.resultSet()) {
                // add/update control and activate it
                plugin.getQueryFactory().insertSyncControl(rs.getTardis_id(), 24, l, 0);
                // do they have a sonic record?
                HashMap<String, Object> wheres = new HashMap<>();
                wheres.put("uuid", event.getPlayer().getUniqueId().toString());
                ResultSetSonic rss = new ResultSetSonic(plugin, wheres);
                HashMap<String, Object> set = new HashMap<>();
                set.put("activated", 1);
                if (rss.resultSet()) {
                    if (!rss.getSonic().isActivated()) {
                        // update it to activated
                        HashMap<String, Object> wherea = new HashMap<>();
                        wherea.put("uuid", uuid);
                        plugin.getQueryFactory().doUpdate("sonic", set, wherea);
                    }
                } else {
                    set.put("uuid", uuid);
                    plugin.getQueryFactory().doInsert("sonic", set);
                }
            } else {
                event.setCancelled(true);
                // only in TARDIS
                plugin.getMessenger().send(p, TardisModule.TARDIS, "NOT_IN_TARDIS");
            }
        }
    }
}
