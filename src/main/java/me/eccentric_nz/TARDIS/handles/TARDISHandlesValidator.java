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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Programming is a process used by Cybermen to control humans. To program a human, the person has to be dead. A control
 * is installed in the person, powered by electricity, turning the person into an agent of the Cybermen. Control over
 * programmed humans can be shorted out by another signal, but that kills whatever might be left of the person.
 *
 * @author eccentric_nz
 */
class TARDISHandlesValidator {

    private final TARDIS plugin;
    private final ItemStack[] program;
    private final Player player;
    private int endCount = 1;
    private int eventCount = 0;

    TARDISHandlesValidator(TARDIS plugin, ItemStack[] program, Player player) {
        this.plugin = plugin;
        this.program = program;
        this.player = player;
    }

    boolean validateDisk() {
        int i = 0;
        for (ItemStack is : program) {
            if (is != null) {
                TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(is.getItemMeta().displayName()));
                switch (thb) {
                    case FOR -> {
                        if (!validateFor(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The FOR loop does not compute!");
                            return false;
                        }
                    }
                    case IF -> {
                        if (!validateIF(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The IF statement does not compute!");
                            return false;
                        }
                    }
                    case VARIABLE -> {
                        if (!validateVar(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The Variable assignment does not compute!");
                            return false;
                        }
                    }
                    case X, Y, Z -> {
                        if (!validateCoordOrMath(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The Coordinate assignment does not compute!");
                            return false;
                        }
                    }
                    case ARTRON, DEATH, DEMATERIALISE, ENTER, EXIT, HADS, LOG_OUT, MATERIALISE, SIEGE_OFF, SIEGE_ON -> {
                        if (eventCount > 0) {
                            plugin.getMessenger().handlesMessage(player, "You can only have one event per program!");
                            return false;
                        }
                        eventCount++;
                    }
                    case ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, MODULO, LESS_THAN, LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL -> {
                        // must be followed by a number (and maybe a variable)
                        if (!validateCoordOrMath(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The Math operation does not compute!");
                            return false;
                        }
                    }
                    case RANDOM -> {
                        // must be followed by a number or preceded by travel
                        ItemStack pre = program[i - 1];
                        TARDISHandlesBlock cede = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(pre.getItemMeta().displayName()));
                        if (!TARDISHandlesBlock.TRAVEL.equals(cede) && !validateCoordOrMath(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The Math operation does not compute!");
                            return false;
                        }
                    }
                    case DOOR -> {
                        // must be followed by =, ==, OPEN, CLOSED, LOCK, UNLOCK
                        if (!validateDoor(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The Door action does not compute!");
                            return false;
                        }
                    }
                    case LIGHTS, SIEGE -> {
                        // must be followed by =, ==, ON, OFF
                        if (!validateOnOff(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The ON / OFF action does not compute!");
                            return false;
                        }
                    }
                    case POWER -> {
                        // must be followed by =, ==, ON, OFF, SHOW, REDSTONE
                        if (!validatePower(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The Power action does not compute!");
                            return false;
                        }
                    }
                    case TRAVEL -> {
                        // must be followed by X, Y, Z, RECHARGER, HOME, RANDOM, Biome disk, Player disk, Save disk, Area disk
                        if (!validateTravel(i + 1)) {
                            plugin.getMessenger().handlesMessage(player, "The Travel destination does not compute!");
                            return false;
                        }
                    }
                    default -> {
                    }
                }
            }
            i++;
        }
        return true;
    }

    private boolean validateVar(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(op.getItemMeta().displayName()));
        if (!thb.getCategory().equals(TARDISHandlesCategory.OPERATOR)) {
            return false;
        }
        ItemStack val = program[start];
        if (val == null) {
            return false;
        }
        TARDISHandlesBlock thbv = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(val.getItemMeta().displayName()));
        return thbv.getCategory().equals(TARDISHandlesCategory.NUMBER);
    }

    private boolean validateCoordOrMath(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(op.getItemMeta().displayName()));
        return thb.getCategory().equals(TARDISHandlesCategory.NUMBER) || thb.equals(TARDISHandlesBlock.SUBTRACTION);
    }

    private boolean validateDoor(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(op.getItemMeta().displayName()));
        return thb.equals(TARDISHandlesBlock.ASSIGNMENT) || thb.equals(TARDISHandlesBlock.EQUALS) || thb.equals(TARDISHandlesBlock.OPEN) || thb.equals(TARDISHandlesBlock.CLOSE) || thb.equals(TARDISHandlesBlock.LOCK) || thb.equals(TARDISHandlesBlock.UNLOCK);
    }

    private boolean validateOnOff(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(op.getItemMeta().displayName()));
        return thb.equals(TARDISHandlesBlock.ASSIGNMENT) || thb.equals(TARDISHandlesBlock.EQUALS) || thb.equals(TARDISHandlesBlock.ON) || thb.equals(TARDISHandlesBlock.OFF);
    }

    private boolean validatePower(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(op.getItemMeta().displayName()));
        return thb.equals(TARDISHandlesBlock.ASSIGNMENT) || thb.equals(TARDISHandlesBlock.EQUALS) || thb.equals(TARDISHandlesBlock.ON) || thb.equals(TARDISHandlesBlock.OFF) || thb.equals(TARDISHandlesBlock.SHOW) || thb.equals(TARDISHandlesBlock.REDSTONE);
    }

    private boolean validateTravel(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(op.getItemMeta().displayName()));
        Material record = op.getType();
        return thb.equals(TARDISHandlesBlock.HOME) || thb.equals(TARDISHandlesBlock.RECHARGER) || thb.equals(TARDISHandlesBlock.X) || thb.equals(TARDISHandlesBlock.Y) || thb.equals(TARDISHandlesBlock.Z) || thb.equals(TARDISHandlesBlock.RANDOM) || record.equals(Material.MUSIC_DISC_CHIRP) || record.equals(Material.MUSIC_DISC_WAIT) || record.equals(Material.MUSIC_DISC_CAT) || record.equals(Material.MUSIC_DISC_BLOCKS);
    }

    private boolean validateFor(int start) {
        int found = 0;
        for (int i = start; i < 36; i++) {
            ItemStack is = program[i];
            if (is == null) {
                if (i == 35) {
                    return found == endCount;
                } else {
                    continue;
                }
            }
            TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(is.getItemMeta().displayName()));
            switch (i - start) {
                case 0 -> { // must be a variable
                    if (!thb.getCategory().equals(TARDISHandlesCategory.VARIABLE)) {
                        return false;
                    }
                }
                case 1 -> { // must be assignment
                    if (!thb.getDisplayName().equals("Assignment operator")) {
                        return false;
                    }
                }
                case 2, 4 -> { // must be a number
                    if (!thb.getCategory().equals(TARDISHandlesCategory.NUMBER)) {
                        return false;
                    }
                }
                case 3 -> { // must be a TO
                    if (!thb.equals(TARDISHandlesBlock.TO)) {
                        return false;
                    }
                }
                case 5 -> { // must be a DO
                    if (!thb.equals(TARDISHandlesBlock.DO)) {
                        return false;
                    }
                }
                default -> { // search for end
                    if (thb.equals(TARDISHandlesBlock.IF)) {
                        endCount++;
                    }
                    if (thb.equals(TARDISHandlesBlock.END)) {
                        if (found++ == endCount) {
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean validateIF(int start) {
        boolean twoConditions = false;
        int found = 0;
        for (int i = start; i < 36; i++) {
            ItemStack is = program[i];
            if (is == null) {
                if (i == 35) {
                    return found == endCount;
                } else {
                    continue;
                }
            }
            TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(is.getItemMeta().displayName()));
            switch (i - start) {
                case 0 -> { // must be an event, variable or selector
                    if (!thb.getCategory().equals(TARDISHandlesCategory.VARIABLE) && !thb.getCategory().equals(TARDISHandlesCategory.SELECTOR) && !thb.getCategory().equals(TARDISHandlesCategory.EVENT)) {
                        return false;
                    }
                }
                case 1 -> { // must be an operator
                    if (!thb.getCategory().equals(TARDISHandlesCategory.OPERATOR)) {
                        return false;
                    }
                }
                case 2 -> { // must be a number or event or variable
                    if (!thb.getCategory().equals(TARDISHandlesCategory.NUMBER) && !thb.getCategory().equals(TARDISHandlesCategory.EVENT) && !thb.getCategory().equals(TARDISHandlesCategory.VARIABLE)) {
                        return false;
                    }
                }
                case 3 -> { // check for second condition
                    if (thb.equals(TARDISHandlesBlock.AND) || thb.equals(TARDISHandlesBlock.OR)) {
                        twoConditions = true;
                    } else {
                        // must be a DO
                        if (!thb.equals(TARDISHandlesBlock.DO)) {
                            return false;
                        }
                    }
                }
                case 4 -> { // must be an event, variable or selector if twoConditions
                    if (twoConditions && (!thb.getCategory().equals(TARDISHandlesCategory.VARIABLE) && !thb.getCategory().equals(TARDISHandlesCategory.SELECTOR) && !thb.getCategory().equals(TARDISHandlesCategory.EVENT))) {
                        return false;
                    } else {
                        if (thb.equals(TARDISHandlesBlock.IF)) {
                            endCount++;
                        }
                        if (thb.equals(TARDISHandlesBlock.END)) {
                            if (found++ == endCount) {
                                return true;
                            }
                        }
                    }
                }
                case 5 -> { // must be an operator if twoConditions
                    if (twoConditions && (!thb.getCategory().equals(TARDISHandlesCategory.OPERATOR))) {
                        return false;
                    } else {
                        if (thb.equals(TARDISHandlesBlock.IF)) {
                            endCount++;
                        }
                        if (thb.equals(TARDISHandlesBlock.END)) {
                            if (found++ == endCount) {
                                return true;
                            }
                        }
                    }
                }
                case 6 -> { // must be a number or event or variable if twoConditions
                    if (twoConditions && (!thb.getCategory().equals(TARDISHandlesCategory.NUMBER) && !thb.getCategory().equals(TARDISHandlesCategory.EVENT) && !thb.getCategory().equals(TARDISHandlesCategory.VARIABLE))) {
                        return false;
                    } else {
                        if (thb.equals(TARDISHandlesBlock.IF)) {
                            endCount++;
                        }
                        if (thb.equals(TARDISHandlesBlock.END)) {
                            if (found++ == endCount) {
                                return true;
                            }
                        }
                    }
                }
                case 7 -> { // must be a DO
                    if (twoConditions && !thb.equals(TARDISHandlesBlock.DO)) {
                        return false;
                    } else {
                        if (thb.equals(TARDISHandlesBlock.IF)) {
                            endCount++;
                        }
                        if (thb.equals(TARDISHandlesBlock.END)) {
                            if (found++ == endCount) {
                                return true;
                            }
                        }
                    }
                }
                default -> { // search for end
                    if (thb.equals(TARDISHandlesBlock.IF)) {
                        endCount++;
                    }
                    if (thb.equals(TARDISHandlesBlock.END)) {
                        if (found++ == endCount) {
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }
}
