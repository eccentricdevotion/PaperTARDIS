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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class EntryDialog {

    public Dialog create(TARDISInfoMenu tardisInfoMenu, TISCategory category) {
        TreeSet<TARDISInfoMenu> entries = getChildren(tardisInfoMenu);
        if (!entries.isEmpty()) {
            CommonDialogData dialogData = new CommonDialogData(Component.literal(tardisInfoMenu.getName()), Optional.empty(), true, true, DialogAction.CLOSE, List.of(), List.of());
            List<ActionButton> actions = new ArrayList<>();
            for (TARDISInfoMenu key : entries) {
                actions.add(makeButton(key));
            }
            CommonButtonData backButton = new CommonButtonData(CommonComponents.GUI_BACK, Optional.empty(), 150);
            Action action = new StaticAction(new ClickEvent.ShowDialog(Holder.direct(new SectionDialog().create(category))));
            return new MultiActionDialog(dialogData, actions, Optional.of(new ActionButton(backButton, Optional.of(action))), 1);
        }
        return null;
    }

    private TreeSet<TARDISInfoMenu> getChildren(TARDISInfoMenu parent) {
        TreeSet<TARDISInfoMenu> children = new TreeSet<>();
        for (TARDISInfoMenu tardisInfoMenu : TARDISInfoMenu.values()) {
            if (!tardisInfoMenu.getParent().isEmpty() && TARDISInfoMenu.valueOf(tardisInfoMenu.getParent()).equals(parent)) {
                children.add(tardisInfoMenu);
            }
        }
        return children;
    }

    private ActionButton makeButton(TARDISInfoMenu tardisInfoMenu) {
        CompoundTag tag = new CompoundTag();
        tag.putString("m", tardisInfoMenu.toString());
        ResourceLocation form = ResourceLocation.fromNamespaceAndPath("tardis", "entry");
        CustomAll action = new CustomAll(form, Optional.of(tag));
        CommonButtonData buttonData = new CommonButtonData(Component.literal(TARDISStringUtils.capitalise(tardisInfoMenu.toString())), Optional.empty(), 200);
        return new ActionButton(buttonData, Optional.of(action));
    }
}
