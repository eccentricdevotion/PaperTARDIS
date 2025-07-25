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
package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIParticle;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.HashMap;
import java.util.List;

/**
 * Retro-genitor particles are a type of radiation that can stop Time Lords from regenerating. They can prevent an
 * individual from travelling in time without a time capsule. Proximity to someone who is infused with retro-genitor
 * particles can cause their eyes to turn dark periodically.
 *
 * @author eccentric_nz
 */
public class TARDISParticleInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final String uuid;
    private final Inventory inventory;

    public TARDISParticleInventory(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Particle Preferences", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Particle Preferences GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        // get particle prefs
        ParticleData data;
        ResultSetParticlePrefs rs = new ResultSetParticlePrefs(plugin);
        if (rs.fromUUID(uuid)) {
            data = rs.getData();
        } else {
            // make a record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid);
            plugin.getQueryFactory().doSyncInsert("particle_prefs", set);
            data = new ParticleData(ParticleEffect.ASH, ParticleShape.RANDOM, 16, 0, "WHITE", "STONE", false);
        }
        // shape
        ItemStack shape = ItemStack.of(GUIParticle.SHAPE_INFO.material(), 1);
        ItemMeta sim = shape.getItemMeta();
        sim.displayName(Component.text("Effect Shape"));
        sim.lore(List.of(Component.text("Choose a shape"), Component.text("from the options"), Component.text("on the right.")));
        shape.setItemMeta(sim);
        stacks[GUIParticle.SHAPE_INFO.slot()] = shape;
        int i = 1;
        // shapes
        for (ParticleShape ps : ParticleShape.values()) {
            Material sm = (data.getShape() == ps) ? Material.LAPIS_ORE : GUIParticle.SHAPE.material();
            ItemStack pshape = ItemStack.of(sm, 1);
            ItemMeta me = pshape.getItemMeta();
            me.displayName(Component.text(TARDISStringUtils.capitalise(ps.toString())));
            pshape.setItemMeta(me);
            stacks[i] = pshape;
            i++;
        }
        // effect
        ItemStack effect = ItemStack.of(GUIParticle.EFFECT_INFO.material(), 1);
        ItemMeta eim = effect.getItemMeta();
        eim.displayName(Component.text("Effect Particle"));
        eim.lore(List.of(Component.text("Choose a particle"), Component.text("from the options"), Component.text("on the right.")));
        effect.setItemMeta(eim);
        stacks[GUIParticle.EFFECT_INFO.slot()] = effect;
        i = 10;
        // effects
        for (ParticleEffect pe : ParticleEffect.values()) {
            if (pe != ParticleEffect.LEAVES) {
                Material pm = (data.getEffect() == pe) ? Material.REDSTONE_ORE : GUIParticle.EFFECT.material();
                ItemStack peffect = ItemStack.of(pm, 1);
                ItemMeta pim = peffect.getItemMeta();
                pim.displayName(Component.text(TARDISStringUtils.capitalise(pe.toString())));
                peffect.setItemMeta(pim);
                stacks[i] = peffect;
                if (i % 9 == 7) {
                    i += 3;
                } else {
                    i++;
                }
            }
        }
        // leaves effect (special position)
        Material lm = (data.getEffect() == ParticleEffect.LEAVES) ? Material.REDSTONE_ORE : GUIParticle.EFFECT.material();
        ItemStack leaves = ItemStack.of(lm, 1);
        ItemMeta lim = leaves.getItemMeta();
        lim.displayName(Component.text("Leaves"));
        leaves.setItemMeta(lim);
        stacks[27] = leaves;
        // colour info
        ItemStack colour_info = ItemStack.of(GUIParticle.COLOUR_INFO.material(), 1);
        ItemMeta ciim = colour_info.getItemMeta();
        ciim.displayName(Component.text("Effect Colour"));
        ciim.lore(List.of(Component.text("Only affects DUST"), Component.text("and EFFECT particles."), Component.text("Click below to cycle through"), Component.text("the 16 Minecraft colours.")));
        colour_info.setItemMeta(ciim);
        stacks[GUIParticle.COLOUR_INFO.slot()] = colour_info;
        // colour
        ItemStack colour = ItemStack.of(GUIParticle.COLOUR.material(), 1);
        ItemMeta cim = colour.getItemMeta();
        cim.displayName(Component.text("Particle Colour"));
        // set to colour pref
        NamedTextColor chatColor = ParticleColour.fromColor(data.getColour());
        String col = ParticleColour.toString(chatColor);
        cim.lore(List.of(Component.text(col, chatColor)));
        colour.setItemMeta(cim);
        stacks[GUIParticle.COLOUR.slot()] = colour;
        // block info
        ItemStack block_info = ItemStack.of(GUIParticle.BLOCK_INFO.material(), 1);
        ItemMeta blim = block_info.getItemMeta();
        blim.displayName(Component.text("Effect Block"));
        blim.lore(List.of(Component.text("Only affects BLOCK particles."), Component.text("Click below to cycle"), Component.text("through different blocks.")));
        block_info.setItemMeta(blim);
        stacks[GUIParticle.BLOCK_INFO.slot()] = block_info;
        // block
        ItemStack block = ItemStack.of(GUIParticle.BLOCK.material(), 1);
        ItemMeta bim = block.getItemMeta();
        bim.displayName(Component.text("Block Type"));
        // set to material pref
        bim.lore(List.of(Component.text(data.getBlockData().getMaterial().toString())));
        block.setItemMeta(bim);
        stacks[GUIParticle.BLOCK.slot()] = block;
        // toggle on/off
        ItemStack toggle = ItemStack.of(GUIParticle.TOGGLE.material(), 1);
        ItemMeta tim = toggle.getItemMeta();
        tim.displayName(Component.text("Particles Enabled"));
        tim.lore(List.of(Component.text(data.isOn() ? "ON" : "OFF")));
        CustomModelDataComponent tcomponent = tim.getCustomModelDataComponent();
        tcomponent.setFloats(data.isOn() ? SwitchVariant.BUTTON_TOGGLE_ON.getFloats() : SwitchVariant.BUTTON_TOGGLE_OFF.getFloats());
        tim.setCustomModelDataComponent(tcomponent);
//        NamespacedKey cmd = (data.isOn()) ? GUIParticle.TOGGLE.key() : SwitchVariant.BUTTON_TOGGLE_OFF.getKey();
        toggle.setItemMeta(tim);
        stacks[GUIParticle.TOGGLE.slot()] = toggle;
        // test
        ItemStack test = ItemStack.of(GUIParticle.TEST.material(), 1);
        ItemMeta xim = test.getItemMeta();
        xim.displayName(Component.text("Test"));
        xim.lore(List.of(Component.text("Display particles"), Component.text("around your TARDIS"), Component.text("with the current settings.")));
        test.setItemMeta(xim);
        stacks[GUIParticle.TEST.slot()] = test;
        // density
        ItemStack density = ItemStack.of(GUIParticle.DENSITY.material(), 1);
        ItemMeta dim = density.getItemMeta();
        dim.displayName(Component.text("Particle Density"));
        dim.lore(List.of(Component.text(data.getDensity(), NamedTextColor.AQUA), Component.text("Has no effect"), Component.text("on some shapes."), Component.text("Range: 8 - 32.")));
        density.setItemMeta(dim);
        stacks[GUIParticle.DENSITY.slot()] = density;
        // speed
        ItemStack speed = ItemStack.of(GUIParticle.SPEED.material(), 1);
        ItemMeta spim = speed.getItemMeta();
        spim.displayName(Component.text("Particle Speed"));
        spim.lore(List.of(Component.text(String.format("%.0f", data.getSpeed() * 10), NamedTextColor.AQUA), Component.text("Range: 0 - 10.")));
        speed.setItemMeta(spim);
        stacks[GUIParticle.SPEED.slot()] = speed;
        // minus
        ItemStack minus = ItemStack.of(GUIParticle.MINUS.material(), 1);
        ItemMeta mim = minus.getItemMeta();
        mim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LESS", "Less")));
        minus.setItemMeta(mim);
        stacks[45] = minus;
        stacks[49] = minus;
        // plus
        ItemStack plus = ItemStack.of(GUIParticle.PLUS.material(), 1);
        ItemMeta pim = plus.getItemMeta();
        pim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MORE", "More")));
        plus.setItemMeta(pim);
        stacks[47] = plus;
        stacks[51] = plus;
        // close
        ItemStack close = ItemStack.of(GUIParticle.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(clim);
        stacks[GUIParticle.CLOSE.slot()] = close;
        return stacks;
    }
}
