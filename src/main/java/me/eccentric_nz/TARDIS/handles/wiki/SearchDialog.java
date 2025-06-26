package me.eccentric_nz.TARDIS.handles.wiki;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.action.CustomAll;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.server.dialog.input.BooleanInput;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.minecraft.server.dialog.input.SingleOptionInput;
import net.minecraft.server.dialog.input.TextInput;

import java.util.List;
import java.util.Optional;

public class SearchDialog {

    public Dialog create() {
        List<Input> inputs = List.of(
                new Input("search", new TextInput(200, Component.literal("I'm looking for:"), true, "", 32,Optional.empty()))
        );
        ResourceLocation form = ResourceLocation.fromNamespaceAndPath("tardis", "wiki");
        CustomAll action = new CustomAll(form, Optional.empty());
        CommonDialogData dialogData = new CommonDialogData(Component.literal("Search the TARDIS Wiki"), Optional.empty(), true, true, DialogAction.CLOSE, List.of(), inputs);
        CommonButtonData button = new CommonButtonData(Component.literal("Search"), Optional.empty(), 150);
        return new NoticeDialog(dialogData, new ActionButton(button, Optional.of(action)));
    }
}
