package me.eccentric_nz.TARDIS.info.processors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.info.dialog.EntryDialog;
import me.eccentric_nz.TARDIS.info.dialog.InfoDialog;
import me.eccentric_nz.TARDIS.info.dialog.RoomInfoDialog;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SectionProcessor {

    private final TARDIS plugin;
    private final Player player;

    public SectionProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void showDialog(String entry) {
        TARDISInfoMenu tardisInfoMenu = TARDISInfoMenu.valueOf(entry);
        Dialog dialog;
        if (tardisInfoMenu.isRoom()) {
            dialog = new RoomInfoDialog().create(plugin, tardisInfoMenu);
        } else if (tardisInfoMenu.isSonicUpgrade()) {
            tardisInfoMenu = TARDISInfoMenu.valueOf(entry + "_INFO");
            dialog = new InfoDialog().create(plugin, tardisInfoMenu);
        } else if ((tardisInfoMenu.isMonster() && tardisInfoMenu != TARDISInfoMenu.K9) || isInfoDirect(tardisInfoMenu)) {
            dialog = new InfoDialog().create(plugin, tardisInfoMenu);
        } else {
            TISCategory category = plugin.getTrackerKeeper().getInfoGUI().get(player.getUniqueId());
            dialog = new EntryDialog().create(tardisInfoMenu, category);
        }
        ServerPlayer p = ((CraftPlayer) player).getHandle();
        p.openDialog(Holder.direct(dialog));
    }

    private boolean isInfoDirect(TARDISInfoMenu tardisInfoMenu) {
        return tardisInfoMenu.isConsole() || tardisInfoMenu.isConsoleBlock()
                || tardisInfoMenu.isTimeTravel() || tardisInfoMenu.isUpdateable();
    }
}
