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
package me.eccentric_nz.tardischemistry.element;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ElementBuilder {

    public static ItemStack getElement(Element element) {
        ItemStack is = ItemStack.of(Material.FEATHER, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text(element.toString()));
        if (element.equals(Element.Unknown)) {
            Component question = Component.text("?");
            im.lore(List.of(question, question));
        } else {
            im.lore(List.of(
                    Component.text(element.getSymbol()),
                    Component.text(element.getAtomicNumber())
            ));
        }
        im.setItemModel(element.getModel());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getElement(int atomicNumber) {
        return getElement(Element.getByAtomicNumber().get(atomicNumber));
    }

    public static ItemStack getElement(String symbol) {
        return getElement(Element.getBySymbol().get(symbol));
    }
}
