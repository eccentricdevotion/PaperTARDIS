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
package me.eccentric_nz.tardissonicblaster;

import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSonicBlasterUtils {

    public static boolean checkBlasterInHand(Player p) {
        ItemStack is = p.getInventory().getItemInMainHand();
        if (is == null || !is.getType().equals(Material.GOLDEN_HOE) || !is.hasItemMeta()) {
            return false;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName()) {
            return false;
        }
        return ComponentUtils.endsWith(im.displayName(), "Sonic Blaster");
    }

    public static float getLineOfSightAngle(Player p) {
        return p.getLocation().getPitch();
    }

    public static double getDistanceToTargetBlock(Location b, Player p) {
        Location l = p.getLocation();
        return b.distance(l);
    }
}
