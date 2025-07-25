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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCount;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.display.TARDISDisplayType;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.worldgen.TARDISChunkGenerator;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

/**
 * Various utility methods.
 * <p>
 * The TARDIS can be programmed to execute automatic functions based on certain
 * conditions. It also automatically repairs after too much damage.
 *
 * @author eccentric_nz
 */
public class TARDISUtils {

    private final TARDIS plugin;
    private final String[] CARDINAL = {"E", "NE", "N", "NW", "W", "SW", "S", "SE"};

    public TARDISUtils(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean compareLocations(Location a, Location b) {
        if (a.getWorld().equals(b.getWorld())) {
            double rd = plugin.getArtronConfig().getDouble("recharge_distance");
            double squared = rd * rd;
            return (a.distanceSquared(b) <= squared);
        }
        return false;
    }

    public boolean canGrowRooms(String chunk) {
        String[] data = chunk.split(":");
        World room_world = TARDISStaticLocationGetters.getWorldFromSplitString(chunk);
        ChunkGenerator gen = room_world.getGenerator();
        String dn = "TARDIS_TimeVortex";
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            dn = plugin.getConfig().getString("creation.default_world_name");
        }
        boolean special = (data[0].equalsIgnoreCase(dn) && gen instanceof TARDISChunkGenerator);
        return (data[0].contains("TARDIS_WORLD_") || special);
    }

    public boolean inTARDISWorld(Player player) {
        // check they are still in the TARDIS world
        World world = plugin.getServer().getWorlds().getFirst();
        String name = "";
        if (player != null && player.isOnline()) {
            world = player.getLocation().getWorld();
            name = world.getName();
        }
        ChunkGenerator gen = world.getGenerator();
        // get default world name
        String dn = "TARDIS_TimeVortex";
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            dn = plugin.getConfig().getString("creation.default_world_name");
        }
        boolean special = ((name.equals(dn) || name.equals("TARDIS_Zero_Room")) && gen instanceof TARDISChunkGenerator);
        return name.equals("TARDIS_WORLD_" + player.getName()) || special;
    }

    public boolean inTARDISWorld(Location loc) {
        // check they are still in the TARDIS world
        World world = loc.getWorld();
        String name = world.getName();
        ChunkGenerator gen = world.getGenerator();
        // get default world name
        String dn = "TARDIS_TimeVortex";
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            dn = plugin.getConfig().getString("creation.default_world_name");
        }
        boolean special = (name.equals(dn) && gen instanceof TARDISChunkGenerator);
        return name.startsWith("TARDIS_WORLD_") || special;
    }

    /**
     * Checks if player has storage record, and update the tardis_id field if
     * they do.
     *
     * @param uuid the player's UUID
     * @param id   the player's TARDIS ID
     */
    public void updateStorageId(String uuid, int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetDiskStorage rss = new ResultSetDiskStorage(plugin, where);
        if (rss.resultSet()) {
            HashMap<String, Object> wherej = new HashMap<>();
            wherej.put("uuid", uuid);
            HashMap<String, Object> setj = new HashMap<>();
            setj.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("storage", setj, wherej);
        }
    }

    /**
     * Gets the chat colour to use on the Police Box sign.
     *
     * @return the configured chat colour
     */
    public NamedTextColor getSignColour() {
        NamedTextColor colour;
        String cc = plugin.getConfig().getString("police_box.sign_colour", "WHITE");
        try {
            colour = NamedTextColor.NAMES.value(cc);
        } catch (IllegalArgumentException e) {
            colour = NamedTextColor.WHITE;
        }
        return colour;
    }

    public int getHighestNetherBlock(World w, int wherex, int wherez) {
        int y = 100;
        Block startBlock = w.getBlockAt(wherex, y, wherez);
        while (!startBlock.getType().isAir()) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
        }
        int air = 0;
        while (startBlock.getType().isAir() && startBlock.getLocation().getBlockY() > 30) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
            air++;
        }
        Material mat = startBlock.getType();
        if (air >= 4 && (plugin.getGeneralKeeper().getGoodNether().contains(mat) || plugin.getPlanetsConfig().getBoolean("planets." + w.getName() + ".false_nether"))) {
            y = startBlock.getLocation().getBlockY() + 1;
        }
        return y;
    }

    public boolean inGracePeriod(Player p, boolean update) {
        boolean inGracePeriod = false;
        // check grace period
        int grace = plugin.getConfig().getInt("travel.grace_period");
        if (grace > 0) {
            ResultSetCount rsc = new ResultSetCount(plugin, p.getUniqueId().toString());
            if (rsc.resultSet()) {
                int grace_count = rsc.getGrace();
                if (grace_count < grace) {
                    inGracePeriod = true;
                    if (update) {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "GRACE_PERIOD", String.format("%d", (grace - (grace_count + 1))));
                        // update the grace count if the TARDIS has travelled
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("uuid", p.getUniqueId().toString());
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("grace", (grace_count + 1));
                        plugin.getQueryFactory().doUpdate("t_count", set, where);
                    }
                }
            }
        }
        return inGracePeriod;
    }

    public List<Entity> getJunkTravellers(Location loc) {
        // spawn an entity
        Entity orb = loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        List<Entity> ents = orb.getNearbyEntities(16.0d, 16.0d, 16.0d);
        orb.remove();
        return ents;
    }

    private String getFacingXZ(Player player) {
        if (player.getFacing() == BlockFace.NORTH) {
            return "-Z";
        }
        if (player.getFacing() == BlockFace.SOUTH) {
            return "+Z";
        }
        if (player.getFacing() == BlockFace.EAST) {
            return "+X";
        }
        if (player.getFacing() == BlockFace.WEST) {
            return "-X";
        }
        return "Error!";
    }

    public String getFacing(Player player) {
        double yaw = player.getLocation().getYaw();
        if (yaw >= 337.5 || (yaw <= 22.5 && yaw >= 0.0) || (yaw >= -22.5 && yaw <= 0.0) || (yaw <= -337.5 && yaw <= 0.0)) {
            return "S";
        }
        if ((yaw >= 22.5 && yaw <= 67.5) || (yaw <= -292.5 && yaw >= -337.5)) {
            return "SW";
        }
        if ((yaw >= 67.5 && yaw <= 112.5) || (yaw <= -247.5 && yaw >= -292.5)) {
            return "W";
        }
        if ((yaw >= 112.5 && yaw <= 157.5) || (yaw <= -202.5 && yaw >= -247.5)) {
            return "NW";
        }
        if ((yaw >= 157.5 && yaw <= 202.5) || (yaw <= -157.5 && yaw >= -202.5)) {
            return "N";
        }
        if ((yaw >= 202.5 && yaw <= 247.5) || (yaw <= -112.5 && yaw >= -157.5)) {
            return "NE";
        }
        if ((yaw >= 247.5 && yaw <= 292.5) || (yaw <= -67.5 && yaw >= -112.5)) {
            return "E";
        }
        if ((yaw >= 292.5 && yaw <= 337.5) || (yaw <= -22.5 && yaw >= -67.5)) {
            return "SE";
        }
        return "Error!";
    }

    public String actionBarFormat(Player player) {
        TARDISDisplayType displayType = plugin.getTrackerKeeper().getDisplay().get(player.getUniqueId());
        return switch (displayType) {
            case BIOME -> displayType.getFormat()
                    .replace("%BIOME%", player.getLocation().getBlock().getBiome().toString());
            case COORDS -> displayType.getFormat()
                    .replace("%X%", String.format("%,d", player.getLocation().getBlockX()))
                    .replace("%Y%", String.format("%,d", player.getLocation().getBlockY()))
                    .replace("%Z%", String.format("%,d", player.getLocation().getBlockZ()));
            case DIRECTION -> displayType.getFormat()
                    .replace("%FACING%", getFacing(player))
                    .replace("%FACING_XZ%", getFacingXZ(player));
            case LOCATOR -> displayType.getFormat()
                    .replace("%DIRECTIONS%", getDirection(player));
            case TARGET_BLOCK -> displayType.getFormat()
                    .replace("%TARGET_BLOCK%", player.getTargetBlock(null, 5).getType().toString());
            case WORLD -> displayType.getFormat()
                    .replace("%WORLD%", player.getLocation().getWorld().getName());
            // ALL
            default -> plugin.getConfig().getString("display.all", "&6X&7%X% &6Y&7%Y% &6Z&7%Z% &6F&7%FACING% (%FACING_XZ%) %TARGET_BLOCK%")
                    .replace("%WORLD%", player.getLocation().getWorld().getName())
                    .replace("%X%", String.format("%,d", player.getLocation().getBlockX()))
                    .replace("%Y%", String.format("%,d", player.getLocation().getBlockY()))
                    .replace("%Z%", String.format("%,d", player.getLocation().getBlockZ()))
                    .replace("%FACING%", getFacing(player))
                    .replace("%FACING_XZ%", getFacingXZ(player))
                    .replace("%YAW%", String.format("%.1f", player.getLocation().getYaw()))
                    .replace("%PITCH%", String.format("%.1f", player.getLocation().getPitch()))
                    .replace("%BIOME%", player.getLocation().getBlock().getBiome().toString())
                    .replace("%TARGET_BLOCK%", player.getTargetBlock(null, 5).getType().toString());
        };
    }

    /**
     * Gets the direction to travel between two locations.
     *
     * @param player the player determining the first location
     * @return a direction to move towards
     */
    private String getDirection(Player player) {
        Location location = player.getLocation();
        Location tardis = plugin.getTrackerKeeper().getLocators().get(player.getUniqueId());
        // check same world
        if (location.getWorld() != tardis.getWorld()) {
            return "TARDIS is located in " + tardis.getWorld().getName();
        }
        Vector playerToEntity = tardis.clone().subtract(location).toVector();
        Vector playerLooking = location.getDirection();
        double x1 = playerToEntity.getX();
        double z1 = playerToEntity.getZ();
        double x2 = playerLooking.getX();
        double z2 = playerLooking.getZ();
        double turn = Math.atan2(x1 * z2 - z1 * x2, x1 * x2 + z1 * z2) * 180 / Math.PI;
        int compass = (((int) Math.round(Math.atan2(location.getX() - tardis.getX(), location.getZ() - tardis.getZ()) / (2 * Math.PI / 8))) + 8) % 8;
        String d = CARDINAL[compass];
        int distance = getHorizontalDistance(location, tardis);
        if (turn >= -22.5 && turn < 22.55) {
            return "↑ " + distance + " blocks " + d;
        } else if (turn >= 22.5 && turn < 67.5) {
            return "↖ " + distance + " blocks " + d;
        } else if (turn >= 67.5 && turn < 112.5) {
            return "← " + distance + " blocks " + d;
        } else if (turn >= 112.5 && turn <= 157.5) {
            return "↙ " + distance + " blocks " + d;
        } else if (turn >= 157.5 && turn <= 180 || turn >= -180 && turn < -157.5) {
            return "↓ " + distance + " blocks " + d;
        } else if (turn >= -157.5 && turn < -112.5) {
            return "↘ " + distance + " blocks " + d;
        } else if (turn >= -112.5 && turn < -67.5) {
            return "→ " + distance + " blocks " + d;
        } else if (turn >= -67.5 && turn < -22.5) {
            return "↗ " + distance + " blocks " + d;
        }
        return d;
    }

    public int getHorizontalDistance(Location first, Location second) {
        double fx = first.getX();
        double fz = first.getZ();
        double sx = second.getX();
        double sz = second.getZ();
        return (int) Math.sqrt(Math.pow(fx - sx, 2) + Math.pow(fz - sz, 2));
    }

    public boolean isCustomModel(ItemStack is) {
        for (String k : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            if (plugin.getCustomModelConfig().getString("models." + k + ".item").equals(is.getType().toString())) {
                return true;
            }
        }
        return false;
    }

    public NamespacedKey getCustomModel(Material material, String variant) {
        for (String k : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            if (plugin.getCustomModelConfig().getString("models." + k + ".item", "CUSTOM").equals(material.toString())) {
                return new NamespacedKey(plugin, TARDISStringUtils.toUnderscoredLowercase(k) + variant);
            }
        }
        return null;
    }
}
