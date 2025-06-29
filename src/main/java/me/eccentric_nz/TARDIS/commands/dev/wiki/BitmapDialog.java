package me.eccentric_nz.TARDIS.commands.dev.wiki;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Optional;

public class BitmapDialog {

    public Dialog create() {
        ResourceLocation model = ResourceLocation.fromNamespaceAndPath("tardis", "chameleon_blue_closed");
        ItemStack blue = new ItemStack(Holder.direct(Items.BLUE_DYE));
        blue.set(DataComponents.ITEM_MODEL, model);
        List<DialogBody> body = List.of(
                new ItemBody(blue, Optional.of(new PlainMessage(Component.literal("ㇺ"), 146)), false, false, 16, 139),
                new PlainMessage(Component.literal("Other text"), 200)
        );
        CommonDialogData dialogData = new CommonDialogData(Component.literal("Bitmap"), Optional.empty(), true, true, DialogAction.CLOSE, body, List.of());
        CommonButtonData button = new CommonButtonData(CommonComponents.GUI_DONE, 150);
        return new NoticeDialog(dialogData, new ActionButton(button, Optional.empty()));
    }
}
