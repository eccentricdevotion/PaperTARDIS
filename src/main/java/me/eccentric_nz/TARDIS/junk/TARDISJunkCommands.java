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
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISJunkCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<>();

    public TARDISJunkCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("create");
        firstArgs.add("delete");
        firstArgs.add("find");
        firstArgs.add("floor");
        firstArgs.add("return");
        firstArgs.add("time");
        firstArgs.add("wall");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisjunk then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisjunk")) {
            if (args.length == 0) {
                // find Junk TARDIS
                return new TARDISJunkFind(plugin).find(sender);
            }
            String first = args[0].toLowerCase(Locale.ROOT);
            if (args.length == 1 && firstArgs.contains(first)) {
                switch (first) {
                    case "create" -> {
                        Player p = null;
                        if (sender instanceof Player) {
                            p = (Player) sender;
                        }
                        if (p == null) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                            return false;
                        }
                        return new TARDISJunkCreator(plugin, p).createJunkTARDIS();
                    }
                    case "find" -> {
                        return new TARDISJunkFind(plugin).find(sender);
                    }
                    case "time" -> {
                        return new TARDISJunkTime(plugin).elapsed(sender);
                    }
                    case "return" -> {
                        return new TARDISJunkReturn(plugin).recall(sender, false);
                    }
                    case "delete" -> {
                        return new TARDISJunkDelete(plugin).delete(sender);
                    }
                }
                return true;
            }
            if (args.length == 2 && first.equals("floor") || first.equals("wall")) {
                return new TARDISJunkFloorWall(plugin).setJunkWallOrFloor(sender, args);
            }
        }
        return false;
    }
}
