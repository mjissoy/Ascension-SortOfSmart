package net.thejadeproject.ascension.refactor_packages.gui.overlay.skill_casting;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;

public class SkillSegment extends RenderableElement {
    public final int slot;
    public final int count;
    public final double radius;

    public SkillSegment(UIFrame frame, int slot, int count, double radius){
        super(frame);
        this.slot = slot;
        this.count = count;
        this.radius = radius;
    }
}
