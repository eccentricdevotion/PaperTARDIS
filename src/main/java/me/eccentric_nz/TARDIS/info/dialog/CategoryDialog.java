package me.eccentric_nz.TARDIS.info.dialog;

import me.eccentric_nz.TARDIS.info.TISCategory;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.action.CustomAll;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDialog {

    public Dialog create() {
        ResourceLocation model = ResourceLocation.fromNamespaceAndPath("tardis", "chameleon_blue_closed");
        ItemStack blue = new ItemStack(Holder.direct(Items.BLUE_DYE));
        blue.set(DataComponents.ITEM_MODEL, model);
        List<DialogBody> body = (List.of(new ItemBody(blue, Optional.empty(), false, false, 16, 16), new PlainMessage(Component.literal("Choose a category below:"), 150)));
        CommonDialogData dialogData = new CommonDialogData(Component.literal("TARDIS Information System"), Optional.empty(), true, true, DialogAction.CLOSE, body, List.of());
        List<ActionButton> actions = new ArrayList<>();
        for (TISCategory category : TISCategory.values()) {
            CompoundTag tag = new CompoundTag();
            tag.putString("c", category.toString());
            ResourceLocation form = ResourceLocation.fromNamespaceAndPath("tardis", "category");
            CustomAll action = new CustomAll(form, Optional.of(tag));
            CommonButtonData buttonData = new CommonButtonData(Component.literal(category.getName()), Optional.of(Component.literal(category.getLore().replace("~", "\n"))), 150);
            ActionButton button = new ActionButton(buttonData, Optional.of(action));
            actions.add(button);
        }
        CommonButtonData doneButton = new CommonButtonData(CommonComponents.GUI_DONE, Optional.empty(), 150);
        return new MultiActionDialog(dialogData, actions, Optional.of(new ActionButton(doneButton, Optional.empty())), 2);
    }
}
