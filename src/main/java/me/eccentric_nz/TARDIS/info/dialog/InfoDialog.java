package me.eccentric_nz.TARDIS.info.dialog;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISDescription;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfoDialog {

    public Dialog create(TARDIS plugin, TARDISInfoMenu tardisInfoMenu) {
        try {
            List<DialogBody> body = new ArrayList<>();
            if (ItemLookup.ITEMS.containsKey(tardisInfoMenu)) {
                InfoIcon infoIcon = ItemLookup.ITEMS.get(tardisInfoMenu);
                ItemStack icon = new ItemStack(Holder.direct(infoIcon.item()));
                icon.set(DataComponents.ITEM_MODEL, infoIcon.model());
                // set custom name
//                icon.set(DataComponents.CUSTOM_NAME, Component.literal("").append(Component.literal(infoIcon.name()).withStyle(ChatFormatting.WHITE)));
                body.add(new ItemBody(icon, Optional.empty(), false, false, 16, 16));
            }
            String description = TARDISDescription.valueOf(tardisInfoMenu.toString()).getDesc();
            body.add(new PlainMessage(Component.literal(description), 200));
            String title = TARDISStringUtils.capitalise(tardisInfoMenu.toString().replace("_INFO", ""));
            CommonDialogData dialogData = new CommonDialogData(Component.literal(title), Optional.empty(), true, true, DialogAction.CLOSE, body, List.of());
            CommonButtonData yesButton = new CommonButtonData(CommonComponents.GUI_BACK, Optional.empty(), 150);
            Action action = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new CategoryDialog().create())));
            CommonButtonData noButton = new CommonButtonData(CommonComponents.GUI_DONE, Optional.empty(), 150);
            return new ConfirmationDialog(dialogData, new ActionButton(yesButton, Optional.of(action)), new ActionButton(noButton, Optional.empty()));
        } catch (IllegalArgumentException e) {
            plugin.debug(tardisInfoMenu.toString());
        }
        return null;
    }
}
