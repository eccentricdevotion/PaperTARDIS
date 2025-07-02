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
package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerUndisguise implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onManualUndisguise(InventoryClickEvent event) {
        if (event.getSlotType().equals(SlotType.ARMOR)) {
            int slot = event.getRawSlot();
            if (slot > 4 && slot < 9) {
                ItemStack is = event.getCurrentItem();
                if (is != null) {
                    if (is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (!im.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER)) {
                            if (im.hasDisplayName() && (
                                    TARDISStringUtils.startsWith(im.displayName(), "Weeping Angel")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Angel Of Liberty")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Clockwork Droid")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Cyberman")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Cybershade")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Dalek Sec")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Dalek")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Davros")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Empty Child")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Hath")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Headless Monk")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Ice Warrior")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Judoon")
                                    || TARDISStringUtils.startsWith(im.displayName(), "K9")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Mire")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Omega")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Ood")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Ossified")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Racnoss")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Scarecrow")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Sea Devil")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Silent")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Silurian")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Slitheen")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Smiler")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Sontaran")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Strax")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Sutekh")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Sycorax")
                                    || TARDISStringUtils.startsWith(im.displayName(), "The Beast")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Toclafane")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Vampire")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Vashta")
                                    || TARDISStringUtils.startsWith(im.displayName(), "Zygon")
                            )) {
                                event.setCancelled(true);
                                TARDIS.plugin.getMessenger().send(event.getWhoClicked(), TardisModule.MONSTERS, "WA_OFF");
                            }
                        }
                    }
                }
            }
        }
    }
}
