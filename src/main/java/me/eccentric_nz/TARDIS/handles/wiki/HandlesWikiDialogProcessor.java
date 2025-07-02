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
package me.eccentric_nz.TARDIS.handles.wiki;

import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlesWikiDialogProcessor {

    private final TARDIS plugin;

    public HandlesWikiDialogProcessor(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void getLinks(CompoundTag query, Player player) {
        Set<WikiLink> results = getWikiLinks(query);
        // build a custom results dialog
        List<ActionButton> actions = new ArrayList<>();
        if (!results.isEmpty()) {
            for (WikiLink w : results) {
                URI uri = URI.create(w.getURL());
                StaticAction action = new StaticAction(new ClickEvent.OpenUrl(uri));
                CommonButtonData buttonData = new CommonButtonData(Component.literal(w.getTitle()), Optional.empty(), 150);
                ActionButton button = new ActionButton(buttonData, Optional.of(action));
                actions.add(button);
            }
        } else {
            CommonButtonData yesButton = new CommonButtonData(Component.literal("No results"), Optional.empty(), 150);
            Action action = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new SearchDialog().create())));
            actions.add(new ActionButton(yesButton, Optional.of(action)));
        }
        List<DialogBody> body = List.of(new PlainMessage(Component.literal("Search results:"), 200));
        CommonDialogData dialogData = new CommonDialogData(Component.literal("TARDIS Wiki"), Optional.empty(), true, true, DialogAction.CLOSE, body, List.of());
        CommonButtonData yesButton = new CommonButtonData(CommonComponents.GUI_BACK, Optional.empty(), 150);
        Action action = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new SearchDialog().create())));
        Dialog dialog = new MultiActionDialog(dialogData, actions, Optional.of(new ActionButton(yesButton, Optional.of(action))),1);
        ServerPlayer receiver = ((CraftPlayer) player).getHandle();
        receiver.openDialog(Holder.direct(dialog));
    }

    private static Set<WikiLink> getWikiLinks(CompoundTag tag) {
        String query = tag.getStringOr("search", "meh!meh");
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        Set<WikiLink> results = new HashSet<>();
        // <a title="Dev commands" href="/commands/dev">Dev commands</a>
        try {
            Document doc = Jsoup.connect("https://tardis.pages.dev/site-map").get();
            Elements links = doc.select("a");
            for (Element e : links) {
                String linkHref = e.attr("href"); // "/commands/dev"
                String linkText = e.text(); // "Dev commands"
                Matcher mat = pattern.matcher(linkText);
                if (mat.find()) {
                    results.add(new WikiLink(linkText, linkHref));
                }
            }
        } catch (IOException ignored) {
        }
        return results;
    }
}
