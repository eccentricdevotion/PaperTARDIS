package me.eccentric_nz.TARDIS.info.dialog;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.StaticAction;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.PlainMessage;

import java.util.List;
import java.util.Optional;

public class RoomInfoDialog {
    

    public Dialog create(TARDIS plugin, TARDISInfoMenu tim) {
        String r = tim.toString();
        Component seed = Component.literal("Seed Block: ").withStyle(ChatFormatting.GOLD).append(Component.literal(plugin.getRoomsConfig().getString("rooms." + r + ".seed")).withStyle(ChatFormatting.RESET));
        Component offset = Component.literal("Offset: ").withStyle(ChatFormatting.GOLD).append(Component.literal(plugin.getRoomsConfig().getString("rooms." + r + ".offset")).withStyle(ChatFormatting.RESET));
        Component cost = Component.literal("Cost: ").withStyle(ChatFormatting.GOLD).append(Component.literal(plugin.getRoomsConfig().getString("rooms." + r + ".cost")).withStyle(ChatFormatting.RESET));
        Component enabled = Component.literal("Enabled: ").withStyle(ChatFormatting.GOLD).append(Component.literal(plugin.getRoomsConfig().getString("rooms." + r + ".enabled")).withStyle(ChatFormatting.RESET));
        List<DialogBody> body = List.of(
                new PlainMessage(seed, 200),
                new PlainMessage(offset, 200),
                new PlainMessage(cost, 200),
                new PlainMessage(enabled, 200)
        );
        String title = TARDISStringUtils.capitalise(r.replace("_", " "));
        CommonDialogData dialogData = new CommonDialogData(Component.literal(title), Optional.empty(), true, true, DialogAction.CLOSE, body, List.of());
        CommonButtonData yesButton = new CommonButtonData(CommonComponents.GUI_BACK, Optional.empty(), 150);
        Action action = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new SectionDialog().create(TISCategory.ROOMS))));
        CommonButtonData noButton = new CommonButtonData(CommonComponents.GUI_DONE, Optional.empty(), 150);
        return new ConfirmationDialog(dialogData, new ActionButton(yesButton, Optional.of(action)), new ActionButton(noButton, Optional.empty()));
    }
}
