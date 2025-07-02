/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TVMRecipe {

    private final TARDIS plugin;

    public TVMRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    // TODO make recipe non-configurable like other TARDIS recipes
    public ShapedRecipe makeRecipe() {
        String[] result_iddata = plugin.getVortexConfig().getString("recipe.result").split(":");
        Material mat = Material.valueOf(result_iddata[0]);
        int amount = plugin.getVortexConfig().getInt("recipe.amount");
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("Vortex Manipulator", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        String vm = plugin.getVortexConfig().getString("recipe.lore", "");
        if (!vm.isEmpty()) {
            List<Component> lore = new ArrayList<>();
            for (String s : vm.split("~")) {
                lore.add(Component.text(s));
            }
            im.lore(lore);
        }
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "vortex-manipulator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // get shape
        try {
            String[] shape_tmp = plugin.getVortexConfig().getString("recipe.shape").split(",");
            String[] shape = new String[3];
            for (int i = 0; i < 3; i++) {
                shape[i] = shape_tmp[i].replaceAll("-", " ");
            }
            r.shape(shape[0], shape[1], shape[2]);
            Set<String> ingredients = plugin.getVortexConfig().getConfigurationSection("recipe.ingredients").getKeys(false);
            ingredients.forEach((g) -> {
                char c = g.charAt(0);
                Material m = Material.valueOf(plugin.getVortexConfig().getString("recipe.ingredients." + g));
                r.setIngredient(c, m);
            });
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.VORTEX_MANIPULATOR, "Recipe failed! Check the config file!");
        }
        // add the recipe to TARDIS' list
        plugin.getFigura().getShapedRecipes().put("Vortex Manipulator", r);
        // return the recipe
        return r;
    }
}
