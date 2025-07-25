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
package me.eccentric_nz.TARDIS.recipes.smithing;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

/*
base:BLAZE_ROD
addition:GLOWSTONE_DUST=Pickup Arrows Circuit
result:BLAZE_ROD

*/

public class PickupArrowsRepairRecipe {

    private final TARDIS plugin;

    public PickupArrowsRepairRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        // result
        ItemStack result = ItemStack.of(Material.BLAZE_ROD, 1);
        // template
        RecipeChoice template = new RecipeChoice.MaterialChoice(Material.REDSTONE);
        // base material to upgrade
        RecipeChoice base = new RecipeChoice.MaterialChoice(Material.BLAZE_ROD);
        // addition
        ItemStack isa = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        ItemMeta ima = isa.getItemMeta();
        ima.displayName(ComponentUtils.toWhite("Pickup Arrows Circuit"));
        CustomModelDataComponent ecomponent = ima.getCustomModelDataComponent();
        ecomponent.setFloats(CircuitVariant.PICKUP.getFloats());
        ima.setCustomModelDataComponent(ecomponent);
        isa.setItemMeta(ima);
        RecipeChoice addition = new RecipeChoice.ExactChoice(isa);
        NamespacedKey key = new NamespacedKey(plugin, "pickup_arrows_repair");
        SmithingRecipe r = new SmithingTransformRecipe(key, result, template, base, addition);
        plugin.getServer().addRecipe(r);
    }
}
