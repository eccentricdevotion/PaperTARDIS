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
package me.eccentric_nz.TARDIS.builders.interior;

import com.google.gson.*;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.utility.FractalFence;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAchievements;
import me.eccentric_nz.TARDIS.desktop.TARDISChunkUtils;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgateDisplaySetter;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.schematic.getters.DataPackPainting;
import me.eccentric_nz.TARDIS.schematic.setters.*;
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;

import java.util.*;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the TARDIS from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInner implements Runnable {

    private final TARDIS plugin;
    private final Schematic schm;
    private final World world;
    private final int dbID;
    private final Player player;
    private final Material wall_type;
    private final Material floor_type;
    private final int tips;
    private final HashMap<Block, BlockData> postBedBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postCarpetBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postDoorBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postDripstoneBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLanternBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLeverBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLichenBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonExtensionBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRedstoneTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRepeaterBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postSculkVeinBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postStickyPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postTorchBlocks = new HashMap<>();
    private final HashMap<Block, JsonObject> postSignBlocks = new HashMap<>();
    private final List<Block> fractalBlocks = new ArrayList<>();
    private final List<Block> iceBlocks = new ArrayList<>();
    private final List<Block> postLightBlocks = new ArrayList<>();
    private final HashMap<Block, TARDISBannerData> postBannerBlocks = new HashMap<>();
    private final HashMap<String, Object> set = new HashMap<>();
    private final HashMap<String, Object> where = new HashMap<>();
    private Block postBedrock = null;
    private Location postOod = null;
    private int task, level = 0, row = 0, startx, starty, startz, resetx, resetz, h, w, d, j = 2;
    private JsonArray arr;
    private JsonObject obj;
    private Location wg1;
    private Location wg2;
    private TARDISTIPSData pos;
    private List<Chunk> chunkList;
    private String playerUUID;
    private boolean running = false;
    private Location ender = null;
    private UseClay use_clay;
    private int counter = 0;
    private double div = 1.0d;
    private BossBar bb;

    /**
     * Builds the inside of the TARDIS.
     *
     * @param plugin     an instance of the main TARDIS plugin class
     * @param schm       the name of the schematic file to use can be ANCIENT, ARS, BIGGER, BONE, BUDGET, CAVE, COPPER,
     *                   CORAL, CURSED, CUSTOM, DELTA, DELUXE, DIVISION, ELEVENTH, ENDER, FACTORY, FIFTEENTH, FUGITIVE,
     *                   HOSPITAL, MASTER, MECHANICAL, ORIGINAL, PLANK, PYRAMID, REDSTONE, ROTOR, RUSTIC, STEAMPUNK,
     *                   THIRTEENTH, TOM, TWELFTH, WAR, WEATHERED, WOOD, LEGACY_BIGGER, LEGACY_DELUXE, LEGACY_ELEVENTH,
     *                   LEGACY_REDSTONE or a CUSTOM name.
     * @param world      the world where the TARDIS is to be built.
     * @param dbID       the unique key of the record for this TARDIS in the database.
     * @param player     an instance of the player who owns the TARDIS.
     * @param wall_type  a material type determined from the TARDIS seed block, this material determines the makeup of
     *                   the TARDIS walls.
     * @param floor_type a material type determined from the TARDIS seed block, this material determines the makeup of
     *                   the TARDIS floors.
     * @param tips       an int determining where this TARDIS will be built ---- -1:own world, > 0:default world ----
     */
    public TARDISBuilderInner(TARDIS plugin, Schematic schm, World world, int dbID, Player player, Material wall_type, Material floor_type, int tips) {
        this.plugin = plugin;
        this.schm = schm;
        this.world = world;
        this.dbID = dbID;
        this.player = player;
        this.wall_type = wall_type;
        this.floor_type = floor_type;
        this.tips = tips;
    }

    @Override
    public void run() {
        if (!running) {
            if (!plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CONFIG_CREATION_WORLD");
                plugin.getServer().getScheduler().cancelTask(task);
                task = -1;
                return;
            }
            starty = schm.getStartY();
            // get JSON
            obj = TARDISSchematicGZip.getObject(plugin, "consoles", schm.getPermission(), schm.isCustom());
            if (obj != null) {
                // get dimensions
                JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                h = dimensions.get("height").getAsInt() - 1;
                w = dimensions.get("width").getAsInt();
                d = dimensions.get("length").getAsInt() - 1;
                div = (h + 1.0d) * w * (d + 1.0d);
                playerUUID = player.getUniqueId().toString();
                // calculate startx, starty, startz
                if (tips > -1000001) {
                    // default world - use TIPS
                    TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                    pos = tintpos.getTIPSData(tips);
                    startx = pos.getCentreX();
                    resetx = pos.getCentreX();
                    startz = pos.getCentreZ();
                    resetz = pos.getCentreZ();
                    // get the correct chunk for ARS
                    Location cl = new Location(world, startx, starty, startz);
                    Chunk c = world.getChunkAt(cl);
                    while (!c.isLoaded()) {
                        c.load(true);
                    }
                    String chun = world.getName() + ":" + c.getX() + ":" + c.getZ();
                    set.put("chunk", chun);
                    if (schm.getPermission().equals("junk")) {
                        set.put("creeper", cl.toString());
                    }
                } else {
                    int[] gsl = plugin.getLocationUtils().getStartLocation(dbID);
                    startx = gsl[0];
                    resetx = gsl[1];
                    startz = gsl[2];
                    resetz = gsl[3];
                }
                wg1 = new Location(world, startx, starty, startz);
                wg2 = new Location(world, startx + (w - 1), starty + (h - 1), startz + (d - 1));
                // get list of used chunks
                chunkList = TARDISChunkUtils.getConsoleChunks(world, wg1.getChunk().getX(), wg1.getChunk().getZ(), w, d);
                // update chunks list in DB
                chunkList.forEach((c) -> {
                    while (!c.isLoaded()) {
                        c.load(true);
                    }
                    HashMap<String, Object> setc = new HashMap<>();
                    setc.put("tardis_id", dbID);
                    setc.put("world", world.getName());
                    setc.put("x", c.getX());
                    setc.put("z", c.getZ());
                    plugin.getQueryFactory().doInsert("chunks", setc);
                });
                where.put("tardis_id", dbID);
                // determine 'use_clay' material
                try {
                    use_clay = UseClay.valueOf(plugin.getConfig().getString("creation.use_clay"));
                } catch (IllegalArgumentException e) {
                    use_clay = UseClay.WOOL;
                }
                // get input array
                arr = obj.get("input").getAsJsonArray();
                // start progress bar
                bb = Bukkit.createBossBar(TARDISConstants.GROWTH_STATES.getFirst(), BarColor.WHITE, BarStyle.SOLID, TARDISConstants.EMPTY_ARRAY);
                bb.setProgress(0);
                bb.addPlayer(player);
                bb.setVisible(true);
                running = true;
            }
        }
        if (level == h && row == w - 1) {
            // put on the door, redstone torches, signs, beds, and the repeaters
            postBedBlocks.forEach(Block::setBlockData);
            postDoorBlocks.forEach(Block::setBlockData);
            postRedstoneTorchBlocks.forEach(Block::setBlockData);
            postTorchBlocks.forEach(Block::setBlockData);
            postLanternBlocks.forEach(Block::setBlockData);
            postRepeaterBlocks.forEach(Block::setBlockData);
            postStickyPistonBaseBlocks.forEach(Block::setBlockData);
            postPistonBaseBlocks.forEach(Block::setBlockData);
            postPistonExtensionBlocks.forEach(Block::setBlockData);
            postLeverBlocks.forEach(Block::setBlockData);
            postDripstoneBlocks.forEach(Block::setBlockData);
            postLichenBlocks.forEach(Block::setBlockData);
            postSculkVeinBlocks.forEach(Block::setBlockData);
            TARDISSignSetter.setSigns(postSignBlocks, plugin, dbID);
            for (Map.Entry<Block, BlockData> carpet : postCarpetBlocks.entrySet()) {
                Block pcb = carpet.getKey();
                pcb.setBlockData(carpet.getValue());
            }
            if (postBedrock != null) {
                postBedrock.setBlockData(TARDISConstants.POWER);
            }
            if (postOod != null) {
                // spawn Ood
                TARDISFollowerSpawner spawner = new TARDISFollowerSpawner(plugin);
                spawner.spawnDivisionOod(postOod);
            }
            postLightBlocks.forEach((block) -> {
                if (block.getType().isAir()) {
                    Levelled levelled = TARDISConstants.LIGHT;
                    levelled.setLevel(15);
                    block.setBlockData(levelled);
                }
            });
            if (schm.getPermission().equals("cave")) {
                iceBlocks.forEach((ice) -> ice.setBlockData(TARDISConstants.WATER));
                iceBlocks.clear();
            }
            for (int f = 0; f < fractalBlocks.size(); f++) {
                FractalFence.grow(fractalBlocks.get(f), f);
            }
            TARDISBannerSetter.setBanners(postBannerBlocks);
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                if (pos != null) {
                    plugin.getWorldGuardUtils().addWGProtection(player, pos, world, schm.getPermission().equals("junk"));
                } else {
                    plugin.getWorldGuardUtils().addWGProtection(player, wg1, wg2);
                }
            }
            if (ender != null) {
                Entity ender_crystal = world.spawnEntity(ender, EntityType.END_CRYSTAL);
                ((EnderCrystal) ender_crystal).setShowingBottom(false);
            }
            if (obj.has("paintings")) {
                JsonArray paintings = (JsonArray) obj.get("paintings");
                for (int i = 0; i < paintings.size(); i++) {
                    JsonObject painting = paintings.get(i).getAsJsonObject();
                    JsonObject rel = painting.get("rel_location").getAsJsonObject();
                    int px = rel.get("x").getAsInt();
                    int py = rel.get("y").getAsInt();
                    int pz = rel.get("z").getAsInt();
                    BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
                    Location pl;
                    String which = painting.get("art").getAsString();
                    Art art = null;
                    if (which.contains(":")) {
                        // custom datapack painting
                        pl = TARDISPainting.calculatePosition(which.split(":")[1], facing, new Location(world, resetx + px, starty + py, resetz + pz));
                    } else {
                        art = RegistryAccess.registryAccess().getRegistry(RegistryKey.PAINTING_VARIANT).get(new NamespacedKey("minecraft", which.toLowerCase(Locale.ROOT)));
                        pl = TARDISPainting.calculatePosition(art, facing, new Location(world, resetx + px, starty + py, resetz + pz));
                    }
                    try {
                        Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
                        ent.teleport(pl);
                        ent.setFacingDirection(facing, true);
                        if (art != null) {
                            ent.setArt(art, true);
                        } else {
                            DataPackPainting.setCustomVariant(ent, which);
                        }
                    } catch (IllegalArgumentException e) {
                        plugin.debug("Invalid painting location!" + pl);
                    }
                }
            }
            if (obj.has("item_frames")) {
                JsonArray frames = obj.get("item_frames").getAsJsonArray();
                for (int i = 0; i < frames.size(); i++) {
                    TARDISItemFrameSetter.curate(frames.get(i).getAsJsonObject(), wg1, dbID);
                }
            }
            if (obj.has("item_displays")) {
                JsonArray displays = obj.get("item_displays").getAsJsonArray();
                for (int i = 0; i < displays.size(); i++) {
                    // set regular blocks for bedrock players
                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                        TARDISFloodgateDisplaySetter.regularBlock(displays.get(i).getAsJsonObject(), wg1, dbID);
                    } else {
                        TARDISItemDisplaySetter.fakeBlock(displays.get(i).getAsJsonObject(), wg1, dbID);
                    }
                }
            }
            // remove dropped items
            chunkList.forEach((chink) -> {
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
            });
            // finished processing - update tardis table!
            plugin.getQueryFactory().doUpdate("tardis", set, where);
            // give kit?
            if (plugin.getKitsConfig().getBoolean("give.create.enabled")) {
                if (TARDISPermission.hasPermission(player, "tardis.kit.create")) {
                    // check if they have the tardis kit
                    HashMap<String, Object> wherek = new HashMap<>();
                    wherek.put("uuid", playerUUID);
                    wherek.put("name", "createkit");
                    ResultSetAchievements rsa = new ResultSetAchievements(plugin, wherek);
                    if (!rsa.resultSet()) {
                        //add a record
                        HashMap<String, Object> setk = new HashMap<>();
                        setk.put("uuid", playerUUID);
                        setk.put("name", "createkit");
                        plugin.getQueryFactory().doInsert("achievements", setk);
                        // give the join kit
                        String kit = plugin.getKitsConfig().getString("give.create.kit");
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisgive " + player.getName() + " kit " + kit);
                    }
                }
            }
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
            bb.setProgress(1);
            bb.setVisible(false);
            bb.removeAll();
        }
        JsonArray floor = arr.get(level).getAsJsonArray();
        JsonArray r = (JsonArray) floor.get(row);
        // paste a column
        for (int col = 0; col <= d; col++) {
            counter++;
            JsonObject c = r.get(col).getAsJsonObject();
            int x = startx + row;
            int y = starty + level;
            int z = startz + col;
            BlockData data = plugin.getServer().createBlockData(c.get("data").getAsString());
            Material type = data.getMaterial();
            if (type.equals(Material.LIGHT_GRAY_CONCRETE) && (schm.getPermission().equals("bone") || schm.getPermission().equals("rustic"))) {
                // get the block
                Block block = new Location(world, x, y, z).getBlock();
                // build a console
                String ct = (schm.getPermission().equals("bone")) ? "console_light_gray" : "console_rustic";
                new ConsoleBuilder(plugin).create(block, ct, dbID, playerUUID);
            }
            if (type.equals(Material.SCULK_SHRIEKER)) {
                // remember the location, so we can make it shriek when flying
                String shrieker = new Location(world, x, y, z).toString();
                TARDISTimeRotor.updateRotorRecord(dbID, shrieker);
            }
            if (type.equals(Material.NOTE_BLOCK)) {
                // remember the location of this Disk Storage
                String storage = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 14, storage, 0);
                if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                    // set block data to BARRIER
                    data = TARDISConstants.BARRIER;
                    // spawn an item display entity
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.DISK_STORAGE, world, x, y, z);
                }
            }
            if (Tag.WOOL.isTagged(type)) {
                switch (type) {
                    case ORANGE_WOOL -> {
                        if (wall_type == Material.ORANGE_WOOL) {
                            switch (use_clay) {
                                case TERRACOTTA -> data = Material.ORANGE_TERRACOTTA.createBlockData();
                                case CONCRETE -> data = Material.ORANGE_CONCRETE.createBlockData();
                                default -> {
                                    if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                                        data = TARDISConstants.BARRIER;
                                        // spawn an item display entity
                                        TARDISDisplayItemUtils.set(TARDISDisplayItem.HEXAGON, world, x, y, z);
                                    }
                                }
                            }
                        } else {
                            data = wall_type.createBlockData();
                        }
                    }
                    case LIGHT_GRAY_WOOL -> {
                        if (!schm.getPermission().equals("eleventh")) {
                            if (floor_type == Material.LIGHT_GRAY_WOOL) {
                                data = switch (use_clay) {
                                    case TERRACOTTA -> Material.LIGHT_GRAY_TERRACOTTA.createBlockData();
                                    case CONCRETE -> Material.LIGHT_GRAY_CONCRETE.createBlockData();
                                    default -> Material.LIGHT_GRAY_WOOL.createBlockData();
                                };
                            } else {
                                data = floor_type.createBlockData();
                            }
                        } else {
                            String[] split = type.toString().split("_");
                            String m;
                            if (split.length > 2) {
                                m = split[0] + "_" + split[1] + "_" + use_clay.toString();
                            } else {
                                m = split[0] + "_" + use_clay.toString();
                            }
                            Material material = Material.getMaterial(m);
                            if (material != null) {
                                data = material.createBlockData();
                            } else {
                                data = TARDISConstants.AIR;
                            }
                        }
                    }
                    case BLUE_WOOL -> {
                        switch (use_clay) {
                            case TERRACOTTA -> data = Material.BLUE_TERRACOTTA.createBlockData();
                            case CONCRETE -> data = Material.BLUE_CONCRETE.createBlockData();
                            default -> {
                                if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                                    data = TARDISConstants.BARRIER;
                                    // spawn an item display entity
                                    TARDISDisplayItemUtils.set(TARDISDisplayItem.BLUE_BOX, world, x, y, z);
                                }
                            }
                        }
                    }
                    default -> {
                        String[] split = type.toString().split("_");
                        String m;
                        if (split.length > 2) {
                            m = split[0] + "_" + split[1] + "_" + use_clay.toString();
                        } else {
                            m = split[0] + "_" + use_clay.toString();
                        }
                        Material material = Material.getMaterial(m);
                        if (material != null) {
                            data = material.createBlockData();
                        } else {
                            data = TARDISConstants.AIR;
                        }
                    }
                }
            }
            if ((type.equals(Material.WARPED_FENCE) || type.equals(Material.CRIMSON_FENCE)) && schm.getPermission().equals("delta")) {
                fractalBlocks.add(world.getBlockAt(x, y, z));
            }
            if (level == 0 && type.equals(Material.PINK_STAINED_GLASS) && schm.getPermission().equals("division")) {
                postLightBlocks.add(world.getBlockAt(x, y - 1, z));
            }
            if (type.equals(Material.DEEPSLATE_REDSTONE_ORE) && (schm.getPermission().equals("division") || schm.getPermission().equals("hospital"))) {
                // replace with gray concrete
                data = schm.getPermission().equals("division") ? Material.GRAY_CONCRETE.createBlockData() : Material.LIGHT_GRAY_CONCRETE.createBlockData();
                if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                    // remember the block to spawn an Ood on
                    postOod = new Location(world, x, y + 1, z);
                }
            }
            if (type.equals(Material.WHITE_STAINED_GLASS) && schm.getPermission().equals("war")) {
                if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                    data = TARDISConstants.BARRIER;
                    // spawn an item display entity
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.ROUNDEL, world, x, y, z);
                }
            }
            if (type.equals(Material.WHITE_TERRACOTTA) && schm.getPermission().equals("war")) {
                if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                    data = TARDISConstants.BARRIER;
                    // spawn an item display entity
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.ROUNDEL_OFFSET, world, x, y, z);
                }
            }
            if (type.equals(Material.SPAWNER)) { // scanner button
                /*
                 * mob spawner will be converted to the correct id by
                 * setBlock(), but remember it for the scanner.
                 */
                String scanner = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 33, scanner, 0);
            }
            if (type.equals(Material.CHEST)) {
                // remember the location of the condenser chest
                String condenser = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 34, condenser, 0);
            }
            if (type.equals(Material.IRON_DOOR)) {
                Bisected bisected = (Bisected) data;
                if (bisected.getHalf().equals(Bisected.Half.BOTTOM)) { // iron door bottom
                    HashMap<String, Object> setd = new HashMap<>();
                    String doorLocation = world.getName() + ":" + x + ":" + y + ":" + z;
                    setd.put("tardis_id", dbID);
                    setd.put("door_type", 1);
                    setd.put("door_location", doorLocation);
                    setd.put("door_direction", "SOUTH");
                    plugin.getQueryFactory().doInsert("doors", setd);
                    // if create_worlds is true, set the world spawn
                    if (plugin.getConfig().getBoolean("creation.create_worlds")) {
                        world.setSpawnLocation(x, y, (z + 1));
                    }
                }
            }
            if (type.equals(Material.STONE_BUTTON) && !schm.getPermission().equals("junk")) { // random button
                // remember the location of this button
                String button = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 1, button, 0);
            }
            if (type.equals(Material.JUKEBOX)) {
                // remember the location of this Advanced Console
                String advanced = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 15, advanced, 0);
                // check if player has storage record, and update the tardis_id field
                plugin.getUtils().updateStorageId(playerUUID, dbID);
                if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                    // set block data to correct BARRIER + Item Display
                    data = TARDISConstants.BARRIER;
                    // spawn an item display entity
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.ADVANCED_CONSOLE, world, x, y, z);
                }
            }
            if (type.equals(Material.CAKE) && !schm.getPermission().equals("junk")) {
                /*
                 * This block will be converted to a lever by
                 * setBlock(), but remember it, so we can use it as the
                 * handbrake! Bone and Rustic have modelled consoles, not a lever handbrake.
                 */
                if (!schm.getPermission().equals("rustic") && !schm.getPermission().equals("bone")) {
                    String handbrakeLocation = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(dbID, 0, handbrakeLocation, 0);
                }
                // create default json for ARS
                String[][][] empty = new String[3][9][9];
                for (int ars_y = 0; ars_y < 3; ars_y++) {
                    for (int ars_x = 0; ars_x < 9; ars_x++) {
                        for (int ars_z = 0; ars_z < 9; ars_z++) {
                            empty[ars_y][ars_x][ars_z] = "STONE";
                        }
                    }
                }
                String control = schm.getSeedMaterial().toString();
                empty[1][4][4] = control;
                switch (schm.getConsoleSize()) {
                    case MASSIVE -> {
                        // the 8 slots on the same level &
                        empty[1][4][5] = control;
                        empty[1][4][6] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                        empty[1][5][6] = control;
                        empty[1][6][4] = control;
                        empty[1][6][5] = control;
                        empty[1][6][6] = control;
                        // the 9 slots on the level above
                        empty[2][4][4] = control;
                        empty[2][4][5] = control;
                        empty[2][4][6] = control;
                        empty[2][5][4] = control;
                        empty[2][5][5] = control;
                        empty[2][5][6] = control;
                        empty[2][6][4] = control;
                        empty[2][6][5] = control;
                        empty[2][6][6] = control;
                    }
                    case WIDE -> {
                        // the 8 slots on the same level
                        empty[1][4][5] = control;
                        empty[1][4][6] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                        empty[1][5][6] = control;
                        empty[1][6][4] = control;
                        empty[1][6][5] = control;
                        empty[1][6][6] = control;
                    }
                    case TALL -> {
                        // the 3 slots on the same level &
                        empty[1][4][5] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                        // the 4 slots on the level above
                        empty[2][4][4] = control;
                        empty[2][4][5] = control;
                        empty[2][5][4] = control;
                        empty[2][5][5] = control;
                    }
                    case MEDIUM -> {
                        // the 3 slots on the same level
                        empty[1][4][5] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                    }
                    default -> {
                        // SMALL size do nothing
                    }
                }
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                JsonArray json = JsonParser.parseString(gson.toJson(empty)).getAsJsonArray();
                HashMap<String, Object> seta = new HashMap<>();
                seta.put("tardis_id", dbID);
                seta.put("uuid", playerUUID);
                seta.put("json", json.toString());
                plugin.getQueryFactory().doInsert("ars", seta);
            }
            if (type.equals(Material.LIGHT)) {
                // remember light block locations for malfunction and light switch
                HashMap<String, Object> setlb = new HashMap<>();
                String lightLocation = world.getName() + ":" + x + ":" + y + ":" + z;
                setlb.put("tardis_id", dbID);
                setlb.put("location", lightLocation);
                plugin.getQueryFactory().doInsert("lamps", setlb);
            }
            if (type.equals(Material.COMMAND_BLOCK) || ((schm.getPermission().equals("bigger") || schm.getPermission().equals("coral") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                /*
                 * command block - remember it to spawn the creeper on.
                 * could also be a beacon block, as the creeper sits
                 * over the beacon in the deluxe and bigger consoles.
                 */
                String creeperLocation = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                set.put("creeper", creeperLocation);
                if (type.equals(Material.COMMAND_BLOCK)) {
                    data = switch (schm.getPermission()) {
                        case "ender" -> Material.END_STONE_BRICKS.createBlockData();
                        case "delta", "cursed" -> Material.BLACKSTONE.createBlockData();
                        case "ancient", "bone", "fugitive" -> Material.GRAY_WOOL.createBlockData();
                        case "hospital" -> Material.LIGHT_GRAY_WOOL.createBlockData();
                        default -> Material.STONE_BRICKS.createBlockData();
                    };
                }
            }
            if (Tag.WOODEN_BUTTONS.isTagged(type) && !schm.getPermission().equals("junk")) {
                /*
                 * wood button - remember it for the Artron Energy
                 * Capacitor.
                 */
                String woodButtonLocation = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 6, woodButtonLocation, 0);
            }
            if (type.equals(Material.DAYLIGHT_DETECTOR)) {
                /*
                 * remember the telepathic circuit.
                 */
                String telepathicLocation = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 23, telepathicLocation, 0);
            }
            if (type.equals(Material.BEACON) && schm.getPermission().equals("ender")) {
                /*
                 * get the ender crystal location
                 */
                ender = world.getBlockAt(x, y, z).getLocation().add(0.5d, 4d, 0.5d);
            }
            // if it's an iron/gold/diamond/emerald/beacon/redstone/bedrock/conduit/netherite block put it in the blocks table
            if (TARDISBuilderInstanceKeeper.getPrecious().contains(type)) {
                HashMap<String, Object> setpb = new HashMap<>();
                String loc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                setpb.put("tardis_id", dbID);
                setpb.put("location", loc);
                setpb.put("data", "minecraft:air");
                setpb.put("police_box", 0);
                plugin.getQueryFactory().doInsert("blocks", setpb);
                plugin.getGeneralKeeper().getProtectBlockMap().put(loc, dbID);
            }
            if (type.equals(Material.ICE) && schm.getPermission().equals("cave")) {
                iceBlocks.add(world.getBlockAt(x, y, z));
            } else if (Tag.BEDS.isTagged(type)) {
                postBedBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.IRON_DOOR)) { // doors
                // if it's the door, don't set it just remember its block then do it at the end
                postDoorBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.REDSTONE_TORCH) || type.equals(Material.REDSTONE_WALL_TORCH)) {
                postRedstoneTorchBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.TORCH) || type.equals(Material.WALL_TORCH) || type.equals(Material.SOUL_TORCH) || type.equals(Material.SOUL_WALL_TORCH)) {
                postTorchBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.STICKY_PISTON)) {
                postStickyPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.PISTON)) {
                postPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.PISTON_HEAD)) {
                postPistonExtensionBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.LEVER)) {
                postLeverBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (Tag.ALL_SIGNS.isTagged(type)) {
                postSignBlocks.put(world.getBlockAt(x, y, z), c);
            } else if (type.equals(Material.POINTED_DRIPSTONE)) {
                postDripstoneBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.GLOW_LICHEN)) {
                postLichenBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.SCULK_VEIN)) {
                postSculkVeinBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.LANTERN) || type.equals(Material.SOUL_LANTERN)) {
                postLanternBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (TARDISStaticUtils.isBanner(type)) {
                JsonObject state = c.has("banner") ? c.get("banner").getAsJsonObject() : null;
                if (state != null) {
                    TARDISBannerData tbd = new TARDISBannerData(data, state);
                    postBannerBlocks.put(world.getBlockAt(x, y, z), tbd);
                }
            } else if (type.equals(Material.PLAYER_HEAD) || type.equals(Material.PLAYER_WALL_HEAD)) {
                TARDISBlockSetters.setBlock(world, x, y, z, data);
                if (c.has("head")) {
                    JsonObject head = c.get("head").getAsJsonObject();
                    if (head.has("uuid")) {
                        try {
                            UUID uuid = UUID.fromString(head.get("uuid").getAsString());
                            TARDISHeadSetter.textureSkull(plugin, uuid, head, world.getBlockAt(x, y, z));
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            } else if (type.equals(Material.DECORATED_POT)) {
                TARDISBlockSetters.setBlock(world, x, y, z, data);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (c.has("pot")) {
                        JsonObject pot = c.get("pot").getAsJsonObject();
                        TARDISPotSetter.decorate(plugin, pot, world.getBlockAt(x, y, z));
                    }
                }, 1L);
            } else if (TARDISStaticUtils.isInfested(type)) {
                // legacy monster egg stone for controls
                TARDISBlockSetters.setBlock(world, x, y, z, Material.VOID_AIR);
            } else if (type.equals(Material.MUSHROOM_STEM)) { // mushroom stem for repeaters
                // save repeater location
                if (j < 6) {
                    String repeater = world.getName() + ":" + x + ":" + y + ":" + z;
                    data = Material.REPEATER.createBlockData();
                    Directional directional = (Directional) data;
                    switch (j) {
                        case 2 -> {
                            directional.setFacing(BlockFace.WEST);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                            plugin.getQueryFactory().insertSyncControl(dbID, 3, repeater, 0);
                        }
                        case 3 -> {
                            directional.setFacing(BlockFace.NORTH);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                            plugin.getQueryFactory().insertSyncControl(dbID, 2, repeater, 0);
                        }
                        case 4 -> {
                            directional.setFacing(BlockFace.SOUTH);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                            plugin.getQueryFactory().insertSyncControl(dbID, 5, repeater, 0);
                        }
                        default -> {
                            directional.setFacing(BlockFace.EAST);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                            plugin.getQueryFactory().insertSyncControl(dbID, 4, repeater, 0);
                        }
                    }
                    j++;
                }
            } else if (type.equals(Material.SPONGE)) {
                TARDISBlockSetters.setBlock(world, x, y, z, Material.VOID_AIR);
            } else if (type.equals(Material.BEDROCK)) {
                // remember bedrock location to block off the beacon light
                String bedrockLocation = world.getName() + ":" + x + ":" + y + ":" + z;
                set.put("beacon", bedrockLocation);
                postBedrock = world.getBlockAt(x, y, z);
            } else if (type.equals(Material.BROWN_MUSHROOM) && schm.getPermission().equals("master")) {
                // spawn locations for two villagers
                TARDISBlockSetters.setBlock(world, x, y, z, Material.VOID_AIR);
                plugin.setTardisSpawn(true);
                world.spawnEntity(new Location(world, x + 0.5, y + 0.25, z + 0.5), EntityType.VILLAGER);
            } else if (type.equals(Material.BLACK_CARPET) && schm.getPermission().equals("master")) {
                postCarpetBlocks.put(world.getBlockAt(x, y, z), data);
            } else {
                TARDISBlockSetters.setBlock(world, x, y, z, data);
            }
            double progress = counter / div;
            if (progress > 1.0) {
                progress = 1.0;
            }
            bb.setProgress(progress);
            if (col == d && row < w) {
                row++;
            }
            if (col == d && row == w && level < h) {
                row = 0;
                level++;
                // set progress bar title
                if (level == h) {
                    bb.setTitle(TARDISConstants.GROWTH_STATES.get(31));
                } else {
                    bb.setTitle(TARDISConstants.GROWTH_STATES.get(level));
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
