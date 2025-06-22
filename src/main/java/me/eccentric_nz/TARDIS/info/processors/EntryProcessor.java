package me.eccentric_nz.TARDIS.info.processors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISRecipe;
import me.eccentric_nz.TARDIS.info.dialog.InfoDialog;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class EntryProcessor {

    private final TARDIS plugin;
    private final Player player;

    public EntryProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void showInfoOrRecipe(String entry) {
        try {
            TARDISInfoMenu tardisInfoMenu = TARDISInfoMenu.valueOf(entry);
            if (entry.endsWith("RECIPE")) {
                new TISRecipe(plugin).show(player, tardisInfoMenu);
            } else {
                plugin.debug(entry);
                Dialog dialog = new InfoDialog().create(plugin, tardisInfoMenu);
                if (dialog != null) {
                    ServerPlayer p = ((CraftPlayer) player).getHandle();
                    p.openDialog(Holder.direct(dialog));
                }
            }
        } catch (IllegalArgumentException e) {
            plugin.debug("[showInfoOrRecipe] '" + entry + "' is not a valid TARDISInfoMenu!");
        }
    }
}
