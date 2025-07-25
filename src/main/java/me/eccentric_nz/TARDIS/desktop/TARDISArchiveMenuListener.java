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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.TARDISThemeButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.schematic.ArchiveUpdate;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * A control room's look could be changed over time. The process by which an operator could transform a control room was
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISArchiveMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISArchiveMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onThemeMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISArchiveInventory)) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("TARDISArchiveMenuListener");
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        InventoryView view = event.getView();
        switch (slot) {
            case 17 -> {
                // back
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", p.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                rs.resultSet();
                Tardis tardis = rs.getTardis();
                // return to Desktop Theme GUI
                close(p);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISThemeButton(plugin, p, tardis.getSchematic(), tardis.getArtronLevel(), tardis.getTardisId()).clickButton(), 2L);
            }
            case 18 -> {
                // size
                ItemStack iss = view.getItem(18);
                ItemMeta ims = iss.getItemMeta();
                List<Component> lores = ims.lore();
                String t;
                String b;
                int s;
                int o = ConsoleSize.valueOf(ComponentUtils.stripColour(lores.getFirst())).ordinal();
                s = (o < 2) ? o + 1 : 0;
                t = ConsoleSize.values()[s].toString();
                b = ConsoleSize.values()[s].getBlocks();
                if (t != null) {
                    ims.lore(List.of(
                            Component.text(t),
                            Component.text(b),
                            Component.text("Click to change", NamedTextColor.AQUA)
                    ));
                    iss.setItemMeta(ims);
                }
            }
            case 19 -> scan(p, view); // scan
            case 20 -> archive(p, view); // archive
            case 22, 23, 24 -> {
                ItemStack template = view.getItem(slot);
                if (template != null) {
                    UUID uuid = p.getUniqueId();
                    TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                    ItemMeta im = template.getItemMeta();
                    String dn = ComponentUtils.stripColour(im.displayName());
                    String size = dn.toLowerCase(Locale.ROOT);
                    int upgrade = plugin.getArtronConfig().getInt("upgrades.template." + size);
                    if (tud.getLevel() >= upgrade) {
                        new ArchiveUpdate(plugin, uuid.toString(), dn).setInUse();
                        tud.setSchematic(Consoles.schematicFor(size));
                        tud.setWall("ORANGE_WOOL");
                        tud.setFloor("LIGHT_GRAY_WOOL");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                            // process upgrade
                            new TARDISThemeProcessor(plugin, uuid).changeDesktop();
                        }, 10L);
                        close(p);
                    }
                }
            }
            case 26 -> close(p); // close
            default -> {
                // get Display name of selected archive
                ItemStack choice = view.getItem(slot);
                if (choice != null) {
                    // remember the upgrade choice
                    Schematic schm = Consoles.schematicFor("archive");
                    UUID uuid = p.getUniqueId();
                    TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                    ItemMeta im = choice.getItemMeta();
                    List<Component> lore = im.lore();
                    if (lore == null || lore.contains(Component.text(plugin.getLanguage().getString("CURRENT_CONSOLE", ""), NamedTextColor.GREEN))) {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "ARCHIVE_NOT_CURRENT");
                        return;
                    }
                    int upgrade = plugin.getArtronConfig().getInt("upgrades.archive.tall");
                    for (Component l : lore) {
                        String c = ComponentUtils.stripColour(l);
                        if (c.startsWith("Cost")) {
                            upgrade = TARDISNumberParsers.parseInt(c.replace("Cost: ", ""));
                        }
                    }
                    if (tud.getLevel() >= upgrade) {
                        new ArchiveUpdate(plugin, uuid.toString(), ComponentUtils.stripColour(im.displayName())).setInUse();
                        tud.setSchematic(schm);
                        tud.setWall("ORANGE_WOOL");
                        tud.setFloor("LIGHT_GRAY_WOOL");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                            // process upgrade
                            new TARDISThemeProcessor(plugin, uuid).changeDesktop();
                        }, 10L);
                        close(p);
                    }
                }
            }
        }
    }

    /**
     * Closes the inventory.
     *
     * @param player the player using the GUI
     */
    @Override
    public void close(Player player) {
        plugin.getTrackerKeeper().getUpgrades().remove(player.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
    }

    /**
     * Closes the inventory and scans the current console.
     *
     * @param p the player using the GUI
     */
    private void scan(Player p, InventoryView view) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            List<Component> lore = getSizeLore(view);
            String size = ComponentUtils.stripColour(lore.getFirst());
            p.closeInventory();
            p.performCommand("tardis archive scan " + size);
        }, 1L);
    }

    /**
     * Closes the inventory and archives the current console. A random name will be generated.
     *
     * @param p the player using the GUI
     */
    private void archive(Player p, InventoryView view) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            List<Component> lore = getSizeLore(view);
            String size = ComponentUtils.stripColour(lore.getFirst());
            p.closeInventory();
            // generate random name
            String name = TARDISRandomArchiveName.getRandomName();
            p.performCommand("tardis archive add " + name + " " + size);
        }, 1L);
    }

    private List<Component> getSizeLore(InventoryView view) {
        ItemStack is = view.getItem(18);
        ItemMeta im = is.getItemMeta();
        return im.lore();
    }
}
