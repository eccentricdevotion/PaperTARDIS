package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.processors.CategoryProcessor;
import me.eccentric_nz.TARDIS.info.processors.SectionProcessor;
import me.eccentric_nz.TARDIS.info.processors.EntryProcessor;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.entity.Player;

public class TARDISInformationSystemProcessor {

    private final TARDIS plugin;
    private final Player player;

    public TARDISInformationSystemProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void process(CompoundTag tag) {
        if (tag.contains("c")) {
            String category = tag.getStringOr("c", "~");
            new CategoryProcessor(plugin, player).showDialog(category);
        }
        if (tag.contains("e")) {
            String entry = tag.getStringOr("e", "~");
            new SectionProcessor(plugin, player).showDialog(entry);
        }
        if (tag.contains("m")) {
            String entry = tag.getStringOr("m", "~");
            if (entry.endsWith("_ITEMS")) {
                new SectionProcessor(plugin, player).showDialog("SKARO_ITEMS");
            } else {
                new EntryProcessor(plugin, player).showInfoOrRecipe(entry);
            }
        }
    }
}
