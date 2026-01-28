package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.screens.EasyGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.guis.easygui.elements.select_skill_active_menu.SkillWheelContainer;
import net.thejadeproject.ascension.network.serverBound.SyncSelectedSkill;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.SkillInputHandler;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class SelectSkillMenu extends EasyGuiScreen {
    private static SelectSkillMenu instance = null;

    public static void open(Component title){
        instance = new SelectSkillMenu(title);
        Minecraft.getInstance().setScreen(instance);
        
    }
    public static void close(){
        //TODO send packet for spell selection
        SkillWheelContainer container = (SkillWheelContainer) instance.getElementByID("skill_wheel_container");
        SkillInputHandler.clearInputHandlers();
        if(container.hoveredSlot != null) {
            Player player = Minecraft.getInstance().player;
            PlayerSkillData.SkillSlot skillSlot = player.getData(ModAttachments.PLAYER_SKILL_DATA).activeSkillContainer.getSkillIdList().get(container.hoveredSlot);

            player.getData(ModAttachments.PLAYER_DATA).setSelectedSkillId(skillSlot.skillId);

            PacketDistributor.sendToServer(new SyncSelectedSkill(container.hoveredSlot));

            if(!skillSlot.equals(PlayerSkillData.SkillSlot.EMPTY_SLOT)){
                ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(skillSlot.skillId);
                if(skill instanceof AbstractActiveSkill activeSkill){
                    activeSkill.initializeInputHandlers();
                }
            }
        }

        instance.onClose();
        instance = null;
    }
    public static boolean hasInstance(){
        return instance != null;
    }



    private SelectSkillMenu(Component title) {
        super(title);
        View view = new View(this);
        addView(view);
        view.setUseMinecraftScale(true);
        SkillWheelContainer container = new SkillWheelContainer(this,0,0);
        view.addChild(container);
        container.setID("skill_wheel_container");
        container.setCustomScale(7);
        


    }
}
