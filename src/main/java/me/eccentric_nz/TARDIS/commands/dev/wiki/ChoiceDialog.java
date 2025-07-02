package me.eccentric_nz.TARDIS.commands.dev.wiki;

import me.eccentric_nz.TARDIS.handles.wiki.SearchDialog;
import me.eccentric_nz.TARDIS.info.dialog.CategoryDialog;
import me.eccentric_nz.TARDIS.travel.dialog.TerminalDialog;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChoiceDialog {

    public Dialog create() {
        CommonButtonData terminalButton = new CommonButtonData(Component.literal("Terminal"), Optional.empty(), 150);
        CommonButtonData infoButton = new CommonButtonData(Component.literal("TIS"), Optional.empty(), 150);
        CommonButtonData wikiButton = new CommonButtonData(Component.literal("Wiki"), Optional.empty(), 150);
        CommonButtonData bitmapButton = new CommonButtonData(Component.literal("Bitmap"), Optional.empty(), 150);
        Action terminalAction = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new TerminalDialog().create())));
        Action infoAction = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new CategoryDialog().create())));
        Action wikiAction = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new SearchDialog().create())));
        Action bitmapAction = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new BitmapDialog().create())));
        CommonDialogData dialogData = new CommonDialogData(Component.literal("Dialogs"), Optional.empty(), true, true, DialogAction.CLOSE, List.of(), List.of());
        List<ActionButton> actions = new ArrayList<>();
        actions.add(new ActionButton(terminalButton, Optional.of(terminalAction)));
        actions.add(new ActionButton(infoButton, Optional.of(infoAction)));
        actions.add(new ActionButton(wikiButton, Optional.of(wikiAction)));
        actions.add(new ActionButton(bitmapButton, Optional.of(bitmapAction)));
        return new MultiActionDialog(dialogData, actions, Optional.empty(), 1);
    }
}
