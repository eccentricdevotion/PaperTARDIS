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
package me.eccentric_nz.tardisweepingangels.monsters.k9;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.K9Variant;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class K9Recipe {

    private final TARDIS plugin;

    public K9Recipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.BONE);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("K9"));
        im.setItemModel(K9Variant.K9.getKey());
        is.setItemMeta(im);
        ShapedRecipe recipe = new ShapedRecipe(TARDISWeepingAngels.K9, is);
        recipe.shape("III", "RRR", "BBB");
        // set ingredients
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('B', Material.BONE);
        plugin.getServer().addRecipe(recipe);
        plugin.getFigura().getShapedRecipes().put("K9", recipe);
    }
}
