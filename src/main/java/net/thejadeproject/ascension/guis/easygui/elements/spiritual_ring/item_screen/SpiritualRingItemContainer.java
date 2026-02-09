package net.thejadeproject.ascension.guis.easygui.elements.spiritual_ring.item_screen;

import net.lucent.easygui.elements.BaseRenderable;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.inventory.DisplayItemHandlerSlot;
import net.lucent.easygui.elements.inventory.DisplaySlot;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.properties.Positioning;
import net.lucent.easygui.util.textures.TextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.screens.SpatialRingItemContainerScreen;
import net.thejadeproject.ascension.guis.menu.SpatialRingItemContainerMenu;

import java.util.ArrayList;

public class SpiritualRingItemContainer extends EmptyContainer {
    private static final ResourceLocation CHEST_GUI = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/spatial_rings/spatial_ring.png");

    private static final ResourceLocation SCROLLBAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/spatial_rings/scroll_bar.png");
    private static final int VISIBLE_ROWS = 6;
    private static final int SLOTS_PER_ROW = 9;
    private static final ITextureData CHEST_TEXTURE = new TextureDataSubSection(CHEST_GUI,256,256,
            0,0,176,96);
    private static final ITextureData TOP_CHEST_TEXTURE = new TextureDataSubSection(CHEST_GUI,256,256,
            0,96,176,112);
    private static final ITextureData ROW_TEXTURE = new TextureDataSubSection(CHEST_GUI,256,256,
            0,112,176,130);

    private ArrayList<BaseRenderable> itemSlots = new ArrayList<>();
    public SpiritualRingItemContainer(IEasyGuiScreen screen,int x,int y){
        super(screen,x,y,0,0);
        setXPositioning(Positioning.CENTER);
        setYPositioning(Positioning.CENTER);
        setX(-CHEST_TEXTURE.getWidth()/2);

        setY(-getHeight()/2);
        setWidth(CHEST_TEXTURE.getWidth());
        setHeight(getHeight());
        createInventory(((SpatialRingItemContainerScreen) screen).getMenu().getTotalRows());
        addPlayerInventory();
        addPlayerHotBar();
    }
    public int getVisibleRows(){
        return Math.min(
                ((SpatialRingItemContainerScreen) screen).getMenu().getVisibleRows(),
            ((SpatialRingItemContainerScreen) screen).getMenu().getTotalRows()
        );
    }
    @Override
    public int getHeight() {
        return CHEST_TEXTURE.getHeight() + TOP_CHEST_TEXTURE.getHeight() + getVisibleRows()*ROW_TEXTURE.getHeight();
    }
    public int getTopHeight(){
        return TOP_CHEST_TEXTURE.getHeight() + getVisibleRows()*ROW_TEXTURE.getHeight();
    }
    public void addPlayerInventory() {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                addChild(new DisplaySlot(getScreen(),8 + l * 18, 14+getTopHeight() + i * 18,16,16,"p_" + (l + i * 9 + 9),false));
            }
        }
    }

    public void addPlayerHotBar() {
        for (int i = 0; i < 9; ++i) {
            addChild(new DisplaySlot(getScreen(),8 + i * 18, 72+getTopHeight(),16,16,"p_"+i,false));

        }
    }

    public void createInventory(int totalRows){
        ItemScrollContainer scrollContainer = new ItemScrollContainer(getScreen(),8,17,18*getVisibleRows());
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < SLOTS_PER_ROW; col++) {
                int slotIndex = row * SLOTS_PER_ROW + col;
                int x =  col * 18;
                int y =  row * 18;
                itemSlots.add(new DisplayItemHandlerSlot(getScreen(),x, y,16,16,"i_"+slotIndex,false));
                scrollContainer.addChild(itemSlots.getLast());


            }
        }
        System.out.println("create a total of : "+ totalRows +" rows");
        addChild(scrollContainer);
    }
    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        int visibleRows=getVisibleRows();
        TOP_CHEST_TEXTURE.renderTexture(guiGraphics);
        for(int i = 0;i<visibleRows;i++){
            ROW_TEXTURE.renderTexture(guiGraphics,0, TOP_CHEST_TEXTURE.getHeight()+i*ROW_TEXTURE.getHeight());
        }
        CHEST_TEXTURE.renderTexture(guiGraphics,0, TOP_CHEST_TEXTURE.getHeight()+visibleRows*ROW_TEXTURE.getHeight());
    }
}
