package net.thejadeproject.ascension.guis.easygui.elements.main_menu.draggable_data;

import net.lucent.easygui.interfaces.IEasyGuiScreen;

public class PhysiqueDataContainer extends DraggableDataContainer{
    public String  physiqueId;
    public PhysiqueDataContainer(IEasyGuiScreen easyGuiScreen, int x, int y,String physiqueId) {
        super(easyGuiScreen, x, y);
        this.physiqueId = physiqueId;
    }
}
