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
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISJunkTime {

    private final TARDIS plugin;

    TARDISJunkTime(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean elapsed(CommandSender sender) {
        // check the Junk TARDIS is not home already
        if (new TARDISJunkLocation(plugin).isNotHome()) {
            long conf = plugin.getConfig().getLong("junk.return");
            if (conf > 0) {
                long waitTime = conf * 1000;
                long lastUsed = new TARDISJunkReturnPersiter(plugin).getJunkTime();
                long now = System.currentTimeMillis();
                long returnTime = (waitTime - (now - lastUsed)) / 1000;
                long mins = returnTime / 60;
                long secs = returnTime - (mins * 60);
                if (secs < 0) {
                    // oops the Junk TARDIS should already be home!
                    new TARDISJunkReturn(plugin).recall(plugin.getConsole(), true);
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "JUNK_RETURN");
                } else {
                    String sub = String.format("%d minutes %d seconds", mins, secs);
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "JUNK_RETURN_TIME", sub);
                }
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "JUNK_NO_RETURN");
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "JUNK_AT_HOME");
        }
        return true;
    }
}
