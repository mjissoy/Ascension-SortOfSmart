package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.screens.EasyGuiScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SetSkillBarScreen extends EasyGuiScreen {
    public SetSkillBarScreen(Component title) {
        super(title);
    }
}
