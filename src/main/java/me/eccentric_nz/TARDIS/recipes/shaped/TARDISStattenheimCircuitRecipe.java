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
package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

/*
easy_shape:---,LRM,QQQ
easy_ingredients.L:GLOWSTONE_DUST=TARDIS Locator Circuit
easy_ingredients.R:REDSTONE
easy_ingredients.M:GLOWSTONE_DUST=TARDIS Materialisation Circuit
easy_ingredients.Q:QUARTZ
hard_shape:---,LRM,QQQ
hard_ingredients.L:GLOWSTONE_DUST=TARDIS Locator Circuit
hard_ingredients.R:REDSTONE
hard_ingredients.M:GLOWSTONE_DUST=TARDIS Materialisation Circuit
hard_ingredients.Q:QUARTZ
result:GLOWSTONE_DUST
amount:1
*/

public class TARDISStattenheimCircuitRecipe {

    private final TARDIS plugin;

    public TARDISStattenheimCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("TARDIS Stattenheim Circuit", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(CircuitVariant.STATTENHEIM.getFloats());
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_stattenheim_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.displayName(Component.text("TARDIS Materialisation Circuit", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        CustomModelDataComponent ecomponent = em.getCustomModelDataComponent();
        ecomponent.setFloats(CircuitVariant.MATERIALISATION.getFloats());
        em.setCustomModelDataComponent(ecomponent);
        // set the second line of lore
        Component uses = (plugin.getConfig().getString("circuits.uses.materialisation", "50").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? Component.text("unlimited", NamedTextColor.YELLOW)
                : Component.text(plugin.getConfig().getString("circuits.uses.materialisation", "50"), NamedTextColor.YELLOW);
        em.lore(List.of(Component.text("Uses left"), uses));
        exact.setItemMeta(em);
        ItemStack locator = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta lim = locator.getItemMeta();
        lim.displayName(Component.text("TARDIS Locator Circuit", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        CustomModelDataComponent lcomponent = lim.getCustomModelDataComponent();
        lcomponent.setFloats(CircuitVariant.LOCATOR.getFloats());
        lim.setCustomModelDataComponent(lcomponent);
        locator.setItemMeta(lim);
        r.shape("LRM", "QQQ");
        r.setIngredient('L', new RecipeChoice.ExactChoice(locator));
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('M', new RecipeChoice.ExactChoice(exact));
        r.setIngredient('Q', Material.QUARTZ);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Stattenheim Circuit", r);
    }
}
