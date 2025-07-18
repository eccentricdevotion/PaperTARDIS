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
package me.eccentric_nz.TARDIS.universaltranslator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author eccentric_nz
 */
public class TARDISTranslateChatListener implements Listener {

    private final TARDIS plugin;

    public TARDISTranslateChatListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTranslateChat(AsyncPlayerChatEvent event) {
        if (plugin.getTrackerKeeper().getTranslators().isEmpty()) {
            return;
        }
        String sender = event.getPlayer().getName(); // the player SENDING the chat message
        for (Player p : event.getRecipients()) {
            if (plugin.getTrackerKeeper().getTranslators().containsKey(p.getUniqueId())) {
                TranslateData data = plugin.getTrackerKeeper().getTranslators().get(p.getUniqueId());
                // should we translate for this sender?
                if ((data != null) && (data.getSender().equals(sender))) {
                    translateChat(p, data.getFrom(), data.getTo(), event.getMessage());
                }
            }
        }
    }

    private void translateChat(Player p, Language from, Language to, String message) {
        try {
            LingvaTranslate translate = new LingvaTranslate(plugin, from.getCode(), to.getCode(), message);
            translate.fetchAsync((hasResult, translated) -> {
                if (hasResult) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().message(p, TardisModule.TRANSLATOR, translated.getTranslated()), 2L);
                }
            });
        } catch (Exception ex) {
            plugin.debug("Could not get translation! " + ex.getMessage());
        }
    }
}
