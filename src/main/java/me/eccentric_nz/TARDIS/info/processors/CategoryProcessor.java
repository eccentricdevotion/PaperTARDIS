package me.eccentric_nz.TARDIS.info.processors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.info.dialog.SectionDialog;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CategoryProcessor {

    private final TARDIS plugin;
    private final Player player;

    public CategoryProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void showDialog(String c) {
        TISCategory category = TISCategory.valueOf(c);
        plugin.getTrackerKeeper().getInfoGUI().put(player.getUniqueId(), category);
        Dialog dialog = new SectionDialog().create(category);
        ServerPlayer p = ((CraftPlayer) player).getHandle();
        p.openDialog(Holder.direct(dialog));
    }
}
