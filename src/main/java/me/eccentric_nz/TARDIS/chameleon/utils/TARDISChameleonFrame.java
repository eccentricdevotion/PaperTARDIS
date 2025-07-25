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
package me.eccentric_nz.TARDIS.chameleon.utils;

import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISChameleonFrame {

    public void updateChameleonFrame(ChameleonPreset preset, String loc) {
        // get location of Chameleon frame
        Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(loc);
        if (location != null) {
            for (Entity e : location.getChunk().getEntities()) {
                if (e instanceof ItemFrame frame) {
                    if (compareLocations(e.getLocation(), location)) {
                        ItemStack is = ItemStack.of(preset.getGuiDisplay());
                        ItemMeta im = is.getItemMeta();
                        im.displayName(Component.text(preset.toString()));
                        is.setItemMeta(im);
                        frame.setItem(is, true);
                        break;
                    }
                }
            }
        }
    }

    private boolean compareLocations(Location one, Location two) {
        return one.getBlockX() == two.getBlockX() && one.getBlockY() == two.getBlockY() && one.getBlockZ() == two.getBlockZ();
    }
}
