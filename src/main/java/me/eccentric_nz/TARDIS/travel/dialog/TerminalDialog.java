package me.eccentric_nz.TARDIS.travel.dialog;

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

import java.util.List;
import java.util.Optional;

public class TerminalDialog {

    public Dialog create() {
        List<DialogBody> body = List.of(new PlainMessage(Component.literal("Set a step distance, the x and z coordinates, a distance multiplier, and a world type."), 200));
        List<Input> inputs = List.of(
                new Input("x", new NumberRangeInput(200, Component.literal("X"), "options.generic_value", new NumberRangeInput.RangeInfo(-500f, 500f, Optional.of(0f), Optional.of(50f)))),
                new Input("z", new NumberRangeInput(200, Component.literal("Z"), "options.generic_value", new NumberRangeInput.RangeInfo(-500f, 500f, Optional.of(0f), Optional.of(50f)))),
                new Input("multiplier", new NumberRangeInput(200, Component.literal("Multiplier"), "options.generic_value", new NumberRangeInput.RangeInfo(1f, 10f, Optional.of(1f), Optional.of(1f)))),
                new Input("environment", new SingleOptionInput(200, List.of(
                        new SingleOptionInput.Entry("CURRENT", Optional.of(Component.literal("Current world")), true),
                        new SingleOptionInput.Entry("NORMAL", Optional.of(Component.literal("An Overworld")), false),
                        new SingleOptionInput.Entry("NETHER", Optional.of(Component.literal("A Nether world")), false),
                        new SingleOptionInput.Entry("THE_END", Optional.of(Component.literal("An End world")), false)
                ), Component.literal("Environment"), true)),
                new Input("submarine", new BooleanInput(Component.literal("Submarine"), false, "true", "false"))
        );
        CompoundTag tag = new CompoundTag();
//        tag.putString("c", category.toString());
        ResourceLocation form = ResourceLocation.fromNamespaceAndPath("tardis", "terminal");
        CustomAll action = new CustomAll(form, Optional.of(tag));
        CommonDialogData dialogData = new CommonDialogData(Component.literal("Destination Terminal"), Optional.empty(), true, true, DialogAction.CLOSE, body, inputs);
        CommonButtonData button = new CommonButtonData(CommonComponents.GUI_CONTINUE, Optional.of(Component.literal("Set destination")), 150);
        return new NoticeDialog(dialogData, new ActionButton(button, Optional.of(action)));
    }
}
