package net.thejadeproject.ascension.guis.easygui.elements.stat_introspection;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.containers.EmptyDraggableContainer;
import net.lucent.easygui.elements.containers.scroll_boxes.DynamicScrollBox;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;
import net.thejadeproject.ascension.guis.easygui.elements.ScrollBox;
import org.w3c.dom.Attr;

import java.util.List;

public class StatViewPanel extends EmptyDraggableContainer {
    private TextureDataSubSection background = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            241,143,356,256
    );
    private TextureDataSubSection panelTitle = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            77,215,150,226
    );
    private TextureDataSubSection healthIcon = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            144,150,151,157
    );
    private TextureDataSubSection armourIcon = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            134,150,143,159
    );
    private TextureDataSubSection armourToughnessIcon = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            124,150,133,159
    );
    private TextureDataSubSection attackDamageIcon = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            162,150,171,159
    );
    private TextureDataSubSection attackSpeedIcon = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            172,150,181,159
    );
    private TextureDataSubSection movementSpeedIcon = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            182,150,191,159
    );
    private TextureDataSubSection jumpStrengthIcon = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            192,150,201,159
    );
    private TextureDataSubSection stepHeightIcon = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            202,150,211,159
    );
    private TextureDataSubSection miningSpeed = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            220,150,229,159
    );
    //TODO move over to config

    public StatViewPanel(IEasyGuiScreen screen, int x,int y){
        super(screen,x,y,115,113);
        ScrollBox contentArea = contentArea();
        addChild(contentArea);
        addChild(new EmptyButton(screen,107,3,7,7) {
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(button == InputConstants.MOUSE_BUTTON_LEFT && clicked){
                    getParent().getParent().removeChild(getParent());
                }
            }
        });
        StatCategoryHolder defensiveStatCategoryHolder = new StatCategoryHolder(screen,0,4,109, Component.literal("Defensive"));
        contentArea.addChild(defensiveStatCategoryHolder);
        defensiveStatCategoryHolder.addChild(
                new AttributeHolder(screen,15,109,healthIcon,Component.literal("VIT : ").withStyle(ChatFormatting.BOLD),Attributes.MAX_HEALTH)
        );
        defensiveStatCategoryHolder.addChild(
                new AttributeHolder(screen,25,109,armourIcon,Component.literal("AC : ").withStyle(ChatFormatting.BOLD),Attributes.ARMOR)
        );
        defensiveStatCategoryHolder.addChild(
                new AttributeHolder(screen,35,109,armourToughnessIcon,Component.literal("TGH : ").withStyle(ChatFormatting.BOLD),Attributes.ARMOR_TOUGHNESS)
        );
        StatCategoryHolder offensiveStatCategoryHolder = new StatCategoryHolder(screen,0,defensiveStatCategoryHolder.getHeight()+4,109,Component.literal("Offensive"));
        contentArea.addChild(offensiveStatCategoryHolder);
        offensiveStatCategoryHolder.addChild(
                new LimiterAttributeHolder(screen,15,109,attackDamageIcon,Component.literal("ATK : ").withStyle(ChatFormatting.BOLD),Attributes.ATTACK_DAMAGE)
        );
        offensiveStatCategoryHolder.addChild(
                new LimiterAttributeHolder(screen,25,109,attackSpeedIcon,Component.literal("ATK SPD : ").withStyle(ChatFormatting.BOLD),Attributes.ATTACK_SPEED)
        );
        offensiveStatCategoryHolder.addChild(
                new LimiterAttributeHolder(screen,35,109,null,Component.literal("ATK KB : ").withStyle(ChatFormatting.BOLD),Attributes.ATTACK_KNOCKBACK)
        );
        StatCategoryHolder miscStatCategoryHolder = new StatCategoryHolder(screen,0,offensiveStatCategoryHolder.getY() + offensiveStatCategoryHolder.getHeight()+5,109,Component.literal("Misc"));
        contentArea.addChild(miscStatCategoryHolder);
        miscStatCategoryHolder.addChild(
                new LimiterAttributeHolder(screen,15,109,movementSpeedIcon,Component.literal("SPD : ").withStyle(ChatFormatting.BOLD),Attributes.MOVEMENT_SPEED)
        );
        miscStatCategoryHolder.addChild(
                new LimiterAttributeHolder(screen,25,109,jumpStrengthIcon,Component.literal("JMP : ").withStyle(ChatFormatting.BOLD),Attributes.JUMP_STRENGTH)
        );
        miscStatCategoryHolder.addChild(
                new LimiterAttributeHolder(screen,35,109,stepHeightIcon,Component.literal("STEP : ").withStyle(ChatFormatting.BOLD),Attributes.STEP_HEIGHT)
        );
        miscStatCategoryHolder.addChild(
                new AttributeHolder(screen,45,109,miningSpeed,Component.literal("MINING SPD : ").withStyle(ChatFormatting.BOLD),Attributes.MINING_EFFICIENCY)
        );
    }
    public ScrollBox contentArea(){
        ScrollBox textArea = new ScrollBox(getScreen(),4,5,109,104){


            @Override
            public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(getScrollBarStartX(),getScrollBarStartY()+getInnerScrollBarRelativeY(),0);
                guiGraphics.fill(0,0,4,getInnerScrollBarHeight(),-13157045);
                guiGraphics.pose().popPose();
            }

            @Override
            public int getScrollBarStartX() {
                return 104;
            }

            @Override
            public int getScrollBarStartY() {
                return 4;
            }

            @Override
            public int getScrollBarWidth() {
                return 4;
            }

            @Override
            public int getScrollRate() {
                return 9;
            }

            @Override
            public int getScrollBarHeight() {
                return 101;
            }
        };


        return textArea;
    }
    public void updateChildPositioning(){

    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        background.renderTexture(guiGraphics);
        panelTitle.renderTexture(guiGraphics,getWidth()/2-panelTitle.getWidth()/2,-6);


    }
}
