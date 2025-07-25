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
package me.eccentric_nz.tardisweepingangels.monsters.judoon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ArrowVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class JudoonAmmoRecipe {

    private final TARDIS plugin;

    public JudoonAmmoRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.ARROW, 2);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Judoon Ammunition"));
        im.setItemModel(ArrowVariant.JUDOON_AMMO.getKey());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "judoon_ammunition");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.ARROW);
        r.addIngredient(Material.GUNPOWDER);
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Judoon Ammunition", r);
    }
}
