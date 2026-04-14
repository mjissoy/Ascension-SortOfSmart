package net.thejadeproject.ascension.gui.elements.skill_view;

public final class SkillMenuState {
    private SkillMenuState() {}

    private static SkillMenuContainer openMenu;

    public static void setOpenMenu(SkillMenuContainer menu) {
        openMenu = menu;
    }

    public static void clearOpenMenu(SkillMenuContainer menu) {
        if (openMenu == menu) {
            openMenu = null;
        }
    }

    public static void markDirty() {
        if (openMenu != null) {
            openMenu.markDirty();
        }
    }
}