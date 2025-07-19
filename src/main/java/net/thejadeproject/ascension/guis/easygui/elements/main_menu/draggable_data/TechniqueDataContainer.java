package net.thejadeproject.ascension.guis.easygui.elements.main_menu.draggable_data;

import net.lucent.easygui.interfaces.IEasyGuiScreen;

public class TechniqueDataContainer extends DraggableDataContainer {
    public String techniqueId;
    public TechniqueDataContainer(IEasyGuiScreen easyGuiScreen, int x, int y,String techniqueId) {
        super(easyGuiScreen, x, y);
        this.techniqueId = techniqueId;
    }
}
