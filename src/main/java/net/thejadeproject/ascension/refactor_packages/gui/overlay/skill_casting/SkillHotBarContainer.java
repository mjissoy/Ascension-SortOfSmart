package net.thejadeproject.ascension.refactor_packages.gui.overlay.skill_casting;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.Minecraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.SkillCastHandler;

public class SkillHotBarContainer extends RenderableElement {
    public SkillHotBarContainer(UIFrame frame) {
        super(frame);

        SkillCastHandler handler = Minecraft.getInstance().player.getData(ModAttachments.ENTITY_DATA).getSkillCastHandler();

        double segmentRadius = 360.0/handler.getMaxSlots();
        int count = 100;
        for(int i=0;i<handler.getMaxSlots();i++){
            addChild(new SkillSegment(frame,i,count,segmentRadius));
        }

    }
    //TODO handle the behaviour for which one is hovered
}
