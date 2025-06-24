package me.eccentric_nz.TARDIS.travel.dialog;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class TARDISTerminalDialogProcessor {

    private final TARDIS plugin;
    private final Player player;

    public TARDISTerminalDialogProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void process(CompoundTag data) {
        if (data != null && !data.isEmpty()) {
            // {environment:"the_end",multiplier:8.0f,submarine:0b,x:200.0f,z:300.0f}
            String environment = data.getStringOr("environment", "current");
            TARDIS.plugin.debug("environment = " + environment);
            boolean submarine = data.getBooleanOr("submarine", false);
            TARDIS.plugin.debug("submarine = " + submarine);
            float x = data.getFloatOr("x", 1.0f);
            TARDIS.plugin.debug("x = " + x);
            float z = data.getFloatOr("z", 1.0f);
            TARDIS.plugin.debug("z = " + z);
            float multiplier = data.getFloatOr("multiplier", 1.0f);
            TARDIS.plugin.debug("multiplier = " + multiplier);
            UUID uuid = player.getUniqueId();
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                Location location;
                switch (environment) {
                    case "CURRENT" -> {
                        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                        if (rsc.resultSet()) {
                            // add coords to current location
                            location = rsc.getCurrent().location().clone().add(x * multiplier, 0, z * multiplier);
                        }
                    }
                    case "NETHER" -> {
                        // get a nether world
                        if (plugin.getConfig().getBoolean("travel.nether") || !plugin.getConfig().getBoolean("travel.terminal.redefine")) {
                            World nether = getWorld("NETHER", player);
                            location =
                        } else {
                            lore = List.of(getWorld(plugin.getConfig().getString("travel.terminal.nether"), current, p));
                        }
                    }
                    case "THE_END" -> {
                        // get an end world
                    }
                    // "NORMAL"
                    default -> {
                        // get an overworld
                    }
                }
            }
        }
    }

    private World getWorld(String environment, Player player) {
        List<World> allowedWorlds = new ArrayList<>();
        String world;
        Set<String> worldlist = plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false);
        for (String o : worldlist) {
//            if (!plugin.getPlanetsConfig().getBoolean("planets." + o + ".enabled")) {
//                continue;
//            }
            if (!plugin.getPlanetsConfig().getBoolean("planets." + o + ".time_travel")) {
                continue;
            }
            if (plugin.getConfig().getBoolean("travel.per_world_perms") && !TARDISPermission.hasPermission(player, "tardis.travel." + o)) {
                continue;
            }
            World ww = TARDISAliasResolver.getWorldFromAlias(o);
            if (ww != null) {
                String env = ww.getEnvironment().toString();
                if (env.equals(env)) {
                    allowedWorlds.add(ww);
                }
            }
        }
        if (!allowedWorlds.isEmpty()) {
            return allowedWorlds.get(TARDISConstants.RANDOM.nextInt(allowedWorlds.size()));
        } else {

        }
        return null;
    }
}
