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
package me.eccentric_nz.TARDIS.advanced;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author original code by comphenix -
 * <a href="https://gist.github.com/aadnk/8138186">...</a>
 */
public class TARDISSerializeInventory {

    public static String itemStacksToString(ItemStack[] stack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // Write the size of the stack
            try (BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
                // Write the size of the stack
                dataOutput.writeInt(stack.length);
                for (ItemStack is : stack) {
                    dataOutput.writeObject(is);
                }
                // Serialize that array
            }
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack[] itemStacksFromString(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            ItemStack[] stack;
            try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
                stack = new ItemStack[dataInput.readInt()];
                // Read the serialized ItemStacks
                for (int i = 0; i < stack.length; i++) {
                    ItemStack is = (ItemStack) dataInput.readObject();
                    if (is != null && is.getType() == Material.GLOWSTONE_DUST) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName() && im.displayName().equals("Circuits")) {
                            CustomModelDataComponent component = im.getCustomModelDataComponent();
                            if (component.getFloats().size() > 0 && component.getFloats().getFirst() != 130.0f) {
                                component.setFloats(List.of(130.0f));
                                im.setCustomModelDataComponent(component);
                                is.setItemMeta(im);
                            }
                        }
                    }
                    stack[i] = is;
                }
            }
            return stack;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
