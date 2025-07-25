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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.ARS.TARDISARSMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.autonomous.TARDISAutonomousInventory;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.config.ConfigDialog;
import me.eccentric_nz.TARDIS.commands.config.TARDISConfigMenuInventory;
import me.eccentric_nz.TARDIS.custommodels.GUIPlayerPreferences;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateMapForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmingInventory;
import me.eccentric_nz.TARDIS.particles.TARDISParticleInventory;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicConfiguratorInventory;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISPrefsMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<String, String> lookup = new HashMap<>();

    public TARDISPrefsMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        lookup.put("Announce Repeaters", "announce_repeaters_on");
        lookup.put("Auto Power Up", "auto_powerup_on");
        lookup.put("Auto-rescue", "auto_rescue_on");
        lookup.put("Autonomous Siege", "auto_siege_on");
        lookup.put("Autonomous", "auto_on");
        lookup.put("Beacon", "beacon_on");
        lookup.put("Close GUI", "close_gui_on");
        lookup.put("Companion Build", "build_on");
        lookup.put("Do Not Disturb", "dnd_on");
        lookup.put("Emergency Programme One", "eps_on");
        lookup.put("Exterior Rendering Room", "renderer_on");
        lookup.put("Hostile Action Displacement System", "hads_on");
        lookup.put("Interior SFX", "sfx_on");
        lookup.put("Info GUI", "info_on");
        lookup.put("Minecart Sounds", "minecart_on");
        lookup.put("Mob Farming", "farm_on");
        lookup.put("Preset Sign", "sign_on");
        lookup.put("Open Display Door", "open_display_door_on");
        lookup.put("Submarine Mode", "submarine_on");
        lookup.put("Telepathic Circuit", "telepathy_on");
        lookup.put("Travel Bar", "travelbar_on");
        lookup.put("Who Quotes", "quotes_on");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrefsMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISPrefsMenuInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 35) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        UUID uuid = p.getUniqueId();
        ItemMeta im = is.getItemMeta();
        if (slot == GUIPlayerPreferences.FORCE_FIELD.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Force Field")) {
            // toggle force field on / off
            if (TARDISPermission.hasPermission(p, "tardis.forcefield")) {
                // check they have upgrade
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(p.getUniqueId().toString(), SystemTree.FORCE_FIELD)) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "SYS_NEED", "Force Field");
                    return;
                }
                List<Component> lore = im.lore();
                boolean bool = (ComponentUtils.stripColour(lore.getFirst()).equals(plugin.getLanguage().getString("SET_OFF", "OFF")));
                if (bool) {
                    // check power
                    ResultSetArtronLevel rsal = new ResultSetArtronLevel(plugin, uuid.toString());
                    if (rsal.resultset()) {
                        if (rsal.getArtronLevel() <= plugin.getArtronConfig().getInt("standby")) {
                            plugin.getMessenger().send(p, TardisModule.TARDIS, "POWER_LOW");
                            return;
                        }
                        if (TARDISForceField.addToTracker(p)) {
                            plugin.getMessenger().send(p, TardisModule.TARDIS, "PREF_WAS_ON", "The TARDIS force field");
                        }
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "POWER_LEVEL");
                        return;
                    }
                } else {
                    plugin.getTrackerKeeper().getActiveForceFields().remove(p.getUniqueId());
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "PREF_WAS_OFF", "The TARDIS force field");
                }
                close(p);
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "NO_PERMS");
            }
            return;
        }
        if (slot == GUIPlayerPreferences.FLIGHT_MODE.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Flight Mode")) {
            List<Component> lore = im.lore();
            // cycle through flight modes
            FlightMode flight = FlightMode.valueOf(ComponentUtils.stripColour(lore.getFirst()));
            int mode = flight.getMode() + 1;
            int limit = (TARDISPermission.hasPermission(p, "tardis.fly")) ? 4 : 3;
            if (mode > limit) {
                mode = 1;
            }
            lore.set(0, Component.text(FlightMode.getByMode().get(mode).toString()));
            im.lore(lore);
            is.setItemMeta(im);
            // set flight mode
            HashMap<String, Object> setf = new HashMap<>();
            setf.put("flying_mode", mode);
            HashMap<String, Object> wheref = new HashMap<>();
            wheref.put("uuid", p.getUniqueId().toString());
            plugin.getQueryFactory().doUpdate("player_prefs", setf, wheref);
            return;
        }
        if (slot == GUIPlayerPreferences.INTERIOR_HUM_SOUND.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Interior Hum Sound")) {
            // close this gui and load the sounds GUI
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                p.closeInventory();
                // open new inventory
                p.openInventory(new TARDISHumInventory(plugin).getInventory());
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.HANDBRAKE.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Handbrake")) {
            // you can only set it to ON!
            List<Component> lore = im.lore();
            if (ComponentUtils.stripColour(lore.getFirst()).equals(plugin.getLanguage().getString("SET_OFF", "OFF"))) {
                // get this player's TARDIS
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (rs.fromUUID(uuid.toString())) {
                    int id = rs.getTardisId();
                    // must not be in the vortex or materialising
                    if (!plugin.getTrackerKeeper().getMaterialising().contains(id) && !plugin.getTrackerKeeper().getInVortex().contains(id)) {
                        // set the handbrake to ON
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("handbrake_on", 1);
                        plugin.getQueryFactory().doUpdate("tardis", set, wheret);
                        im.lore(List.of(Component.text(plugin.getLanguage().getString("SET_ON", "ON"))));
                        is.setItemMeta(im);
                        // Check if it's at a recharge point
                        TARDISArtronLevels tal = new TARDISArtronLevels(plugin);
                        tal.recharge(id);
                        // Remove energy from TARDIS and sets database
                        plugin.getMessenger().sendStatus(p, "HANDBRAKE_ON");
                        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                            int amount = plugin.getTrackerKeeper().getHasDestination().get(id).cost() * -1;
                            HashMap<String, Object> wheref = new HashMap<>();
                            wheref.put("tardis_id", id);
                            plugin.getQueryFactory().alterEnergyLevel("tardis", amount, wheref, p);
                        }
                        plugin.getTrackerKeeper().getHasDestination().remove(id);
                        if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                            plugin.getTrackerKeeper().getHasRandomised().removeAll(Collections.singleton(id));
                        }
                        // damage the circuit if configured
                        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                            // decrement uses
                            int uses_left = tcc.getMaterialisationUses();
                            new TARDISCircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, p).damage();
                        }
                    } else {
                        plugin.getMessenger().sendStatus(p, "HANDBRAKE_IN_VORTEX");
                    }
                } else {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "NO_TARDIS");
                }
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "SONIC_HANDBRAKE_ON");
            }
            return;
        }
        if (slot == GUIPlayerPreferences.TARDIS_MAP.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("TARDIS Map")) {
            // must be in the TARDIS
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
            if (rs.resultSet()) {
                // close this gui and load the TARDIS map
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(uuid)) {
                        new FloodgateMapForm(plugin, uuid, rs.getTardis_id()).send();
                    } else {
                        // close inventory
                        p.closeInventory();
                        // open new inventory
                        p.openInventory(new TARDISARSMap(plugin).getInventory());
                    }
                }, 1L);
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "NOT_IN_TARDIS");
            }
            return;
        }
        if (slot == GUIPlayerPreferences.AUTONOMOUS_PREFERENCES.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Autonomous Preferences")) {
            // close this gui and load the TARDIS Autonomous Menu
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                p.closeInventory();
                // open new inventory
                p.openInventory(new TARDISAutonomousInventory(plugin, uuid).getInventory());
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.FARMING_PREFERENCES.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Farming Preferences")) {
            // close this gui and load the TARDIS Farming Menu
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                p.closeInventory();
                // open new inventory
                p.openInventory(new TARDISFarmingInventory(plugin, uuid).getInventory());
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.SONIC_CONFIGURATOR.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Sonic Configurator")) {
            // close this gui and load the Sonic Configurator
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                p.closeInventory();
                // open new inventory
                p.openInventory(new TARDISSonicConfiguratorInventory(plugin).getInventory());
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.PARTICLES.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Materialisation Particles")) {
            // close this gui and load the Particle Preferences
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                p.closeInventory();
                // open new inventory
                p.openInventory(new TARDISParticleInventory(plugin, uuid.toString()).getInventory());
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.ADMIN_MENU.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Admin Config Menu")) {
            // close this gui and load the Admin Menu
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                if (rsp.resultSet() && rsp.isDialogsOn()) {
                    // close inventory
                    p.closeInventory();
                    Audience.audience(p).showDialog(new ConfigDialog(plugin).create());
                } else {
                    p.openInventory(new TARDISConfigMenuInventory(plugin).getInventory());
                }
            }, 1L);
            return;
        }
        List<Component> lore = im.lore();
        boolean bool = (ComponentUtils.stripColour(lore.getFirst()).equals(plugin.getLanguage().getString("SET_ON", "ON")));
        String value = (bool) ? plugin.getLanguage().getString("SET_OFF", "OFF") : plugin.getLanguage().getString("SET_ON", "ON");
        int b = (bool) ? 0 : 1;
        String which = ComponentUtils.stripColour(im.displayName());
        switch (which) {
            case "Junk TARDIS" -> {
                // must be on the outside of the TARDIS
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("uuid", uuid);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                if (rst.resultSet()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "JUNK_PRESET_OUTSIDE");
                    return;
                }
                if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(uuid)) {
                    long now = System.currentTimeMillis();
                    long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
                    long then = plugin.getTrackerKeeper().getRebuildCooldown().get(uuid) + cooldown;
                    if (now < then) {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "COOLDOWN", String.format("%d", cooldown / 1000));
                        return;
                    }
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                ResultSetJunk rsj = new ResultSetJunk(plugin, where);
                boolean has = rsj.resultSet();
                // get preset
                HashMap<String, Object> wherep = new HashMap<>();
                wherep.put("uuid", uuid.toString());
                ResultSetTardis rsp = new ResultSetTardis(plugin, wherep, "", false);
                if (rsp.resultSet()) {
                    Tardis tardis = rsp.getTardis();
                    String current = tardis.getPreset().toString();
                    // make sure is opposite
                    if (current.equals("JUNK_MODE") && !bool) {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "JUNK_ALREADY_ON");
                        return;
                    }
                    if (!current.equals("JUNK_MODE") && bool) {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "JUNK_ALREADY_OFF");
                        return;
                    }
                    int id = tardis.getTardisId();
                    String cham_set;
                    HashMap<String, Object> setj = new HashMap<>();
                    if (has) {
                        // update record with current preset
                        HashMap<String, Object> wherej = new HashMap<>();
                        wherej.put("uuid", uuid.toString());
                        setj.put("preset", current);
                        plugin.getQueryFactory().doSyncUpdate("junk", setj, wherej);
                    } else {
                        // create a junk record
                        setj.put("uuid", uuid.toString());
                        setj.put("tardis_id", id);
                        setj.put("preset", current);
                        plugin.getQueryFactory().doSyncInsert("junk", setj);
                    }
                    HashMap<String, Object> whereu = new HashMap<>();
                    whereu.put("uuid", uuid.toString());
                    HashMap<String, Object> sett = new HashMap<>();
                    String message = "JUNK_PRESET_ON";
                    if (bool) {
                        // restore saved preset
                        String preset = (has) ? rsj.getPreset().toString() : current;
                        sett.put("chameleon_preset", preset);
                        sett.put("chameleon_demat", "JUNK_MODE");
                        message = "JUNK_PRESET_OFF";
                        cham_set = preset;
                        plugin.getTrackerKeeper().getJunkPlayers().remove(uuid);
                    } else {
                        // save JUNK_MODE preset
                        sett.put("chameleon_preset", "JUNK_MODE");
                        sett.put("chameleon_demat", current);
                        // set chameleon adaption to OFF
                        sett.put("adapti_on", 0);
                        cham_set = "JUNK_MODE";
                        plugin.getTrackerKeeper().getJunkPlayers().put(uuid, id);
                    }
                    plugin.getQueryFactory().doSyncUpdate("tardis", sett, whereu);
                    // set the Chameleon Circuit sign(s)
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    wherec.put("type", 31);
                    ResultSetControls rsc = new ResultSetControls(plugin, wherec, true);
                    if (rsc.resultSet()) {
                        for (HashMap<String, String> map : rsc.getData()) {
                            TARDISStaticUtils.setSign(map.get("location"), 3, cham_set, p);
                        }
                    }
                    // rebuild
                    plugin.getMessenger().send(p, TardisModule.TARDIS, message);
                    p.performCommand("tardis rebuild");
                }
            }
            case "Companion Build" -> {
                String[] args = new String[2];
                args[0] = "";
                args[1] = value;
                new TARDISBuildCommand(plugin).toggleCompanionBuilding(p, args);
            }
            case "Lock Containers" -> {
                if (bool) {
                    plugin.getWorldGuardUtils().unlockContainers(p.getWorld(), p.getName());
                } else {
                    plugin.getWorldGuardUtils().lockContainers(p.getWorld(), p.getName());
                }
            }
            default -> {
                HashMap<String, Object> set = new HashMap<>();
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                if (ComponentUtils.stripColour(im.displayName()).equals("HADS Type")) {
                    value = (ComponentUtils.stripColour(lore.getFirst()).equals("DISPLACEMENT")) ? "DISPERSAL" : "DISPLACEMENT";
                    set.put("hads_type", value);
                } else {
                    set.put(lookup.get(ComponentUtils.stripColour(im.displayName())), b);
                }
                plugin.getQueryFactory().doUpdate("player_prefs", set, where);
            }
        }
        lore.set(0, Component.text(value));
        im.lore(lore);
        GUIPlayerPreferences gui = GUIPlayerPreferences.fromString(which);
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(value.equals(plugin.getLanguage().getString("SET_ON", "ON")) ? gui.getOnFloats() : gui.getOffFloats());
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
        if (ComponentUtils.stripColour(im.displayName()).equals("Beacon")) {
            // get tardis id
            ResultSetTardisID rsi = new ResultSetTardisID(plugin);
            if (rsi.fromUUID(uuid.toString())) {
                new TARDISBeaconToggler(plugin).flickSwitch(uuid, rsi.getTardisId(), !bool);
            }
        }
    }
}
