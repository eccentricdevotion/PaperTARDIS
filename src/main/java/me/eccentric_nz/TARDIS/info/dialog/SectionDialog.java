package me.eccentric_nz.TARDIS.info.dialog;

import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.action.Action;
import net.minecraft.server.dialog.action.CustomAll;
import net.minecraft.server.dialog.action.StaticAction;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.PlainMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectionDialog {

    public Dialog create(TISCategory category) {
        List<DialogBody> body = List.of(new PlainMessage(Component.literal(category.getLore().replace("~", "\n")), 150));
        CommonDialogData dialogData = new CommonDialogData(Component.literal(category.getName()), Optional.empty(), true, true, DialogAction.CLOSE, body, List.of());
        List<ActionButton> actions = new ArrayList<>();
        for (TARDISInfoMenu tardisInfoMenu : TARDISInfoMenu.values()) {
            if (category == TISCategory.ITEMS && tardisInfoMenu.isItem()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.CONSOLE_BLOCKS && tardisInfoMenu.isConsoleBlock()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.ACCESSORIES && tardisInfoMenu.isAccessory()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.COMPONENTS && tardisInfoMenu.isComponent()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.SONIC_COMPONENTS && tardisInfoMenu.isSonicComponent()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.SONIC_UPGRADES && tardisInfoMenu.isSonicUpgrade()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.CONSOLES && tardisInfoMenu.isConsole()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.DISKS && tardisInfoMenu.isDisk()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.ROOMS && tardisInfoMenu.isRoom()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.PLANETS && tardisInfoMenu.isPlanet()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.TIME_TRAVEL && tardisInfoMenu.isTimeTravel()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.FOOD && tardisInfoMenu.isFood()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.UPDATEABLE_BLOCKS && tardisInfoMenu.isUpdateable()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.MONSTERS && tardisInfoMenu.isMonster()) {
                actions.add(makeButton(tardisInfoMenu));
            }
        }
        CommonButtonData exitButton = new CommonButtonData(CommonComponents.GUI_BACK, Optional.empty(), 150);
        Action action = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new CategoryDialog().create())));
        return new MultiActionDialog(dialogData, actions, Optional.of(new ActionButton(exitButton, Optional.of(action))), 2);
    }

    private ActionButton makeButton(TARDISInfoMenu tardisInfoMenu) {
        CompoundTag tag = new CompoundTag();
        tag.putString("e", tardisInfoMenu.toString());
        ResourceLocation form = ResourceLocation.fromNamespaceAndPath("tardis", "entry");
        CustomAll action = new CustomAll(form, Optional.of(tag));
        CommonButtonData buttonData = new CommonButtonData(Component.literal(TARDISStringUtils.capitalise(tardisInfoMenu.toString())), Optional.empty(), 150);
        return new ActionButton(buttonData, Optional.of(action));
    }
}
