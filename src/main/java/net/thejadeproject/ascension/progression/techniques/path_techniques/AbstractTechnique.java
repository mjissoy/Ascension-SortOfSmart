package net.thejadeproject.ascension.progression.techniques.path_techniques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.constants.CultivationSource;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.guis.easygui.elements.HoverableLabel;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.skills.skill_lists.IAcquirableSkill;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.progression.techniques.data.ITechniqueData;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.util.*;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractTechnique implements ITechnique {
    public String title;
    public Double baseRate;

    public Map<String,Double> daoBonuses = new HashMap<>();
    public String path;
    public SkillList skillList = new SkillList(List.of());
    public ITextureData techniqueImage;
    public Component description = Component.empty();
    public StabilityHandler stabilityHandler;
    public IBreakthroughHandler breakthroughHandler;
    public IRealmChangeHandler realmChangeHandler;

    private Supplier<ITechniqueData> dataSupplier = ()-> null;

    public AbstractTechnique(String title, double baseRate, String path, StabilityHandler stabilityHandler, IBreakthroughHandler handler,IRealmChangeHandler realmChangeHandler){
        this.title = title;
        this.baseRate = baseRate;
        this.path = path;
        this.stabilityHandler =stabilityHandler;
        this.breakthroughHandler =handler;
        this.realmChangeHandler = realmChangeHandler;
    }


    @Override
    public IBreakthroughHandler getBreakthroughHandler() {
        return breakthroughHandler;
    }
    @Override
    public IRealmChangeHandler getRealmChangeHandler() {
        return realmChangeHandler;
    }
    @Override
    public StabilityHandler getStabilityHandler() {
        return stabilityHandler;
    }

    @Override
    public void tryCultivate(Player player, CultivationSource source) {
        if(player.level().isClientSide()) return;
        if(!CultivationSystem.cultivate(player,getPath(),baseRate,getCultivationAttributes(),source)) tryStabiliseRealm(player,source);
  
    }

    @Override
    public void tryStabiliseRealm(Player player,CultivationSource source) {
        CultivationSystem.stabiliseRealm(stabilityHandler,player,getPath(),getCultivationAttributes(),0);
    }

    @Override
    public void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event) {

        for(String techniqueAttributeId : getDaoBonuses()){
            event.tryAddDao(techniqueAttributeId,getDaoBonus(techniqueAttributeId));
        }

    }

    public AbstractTechnique setEfficiencyAttributes(Map<String,Double> efficiencyBonuses){
        this.daoBonuses = efficiencyBonuses;
        return this;
    }
    @Override
    public Set<String> getDaoBonuses() {
        return daoBonuses.keySet();
    }
    @Override
    public Double getDaoBonus(String attribute) {
        return daoBonuses.get(attribute);
    }
    @Override
    public String getPath() {
        return path;
    }

    public Component getDisplayTitle(){
        return Component.literal(title);
    }

    @Override
    public SkillList getSkillList() {
        return skillList;
    }
    public AbstractTechnique setSkillList(List<IAcquirableSkill> skillList){
        this.skillList = new SkillList(skillList);
        return this;
    }
    public double getBaseRate(){
        return baseRate;
    }
    public double getRate(){
        return  getBaseRate();
    }

    @Override
    public ITextureData getTechniqueImage() {
        return techniqueImage;
    }

    public void setTechniqueImage(ITextureData techniqueImage) {
        this.techniqueImage = techniqueImage;
    }

    @Override
    public void onRealmChangeEvent(RealmChangeEvent event) {
        System.out.println("Handler: " + System.identityHashCode(event));
        System.out.println("realm changed");
        System.out.println("major : "+event.oldMajorRealm +"->"+event.newMajorRealm);
        System.out.println("minor : "+event.oldMinorRealm +"->"+event.newMinorRealm);
        updatePlayerSkills(event);
        ITechnique.super.onRealmChangeEvent(event);
    }

    @Override
    public void onRemoveTechnique(Player player,ITechniqueData data) {

    }

    //TODO add some sort of path registry and use that for this
    @Override
    public void onTechniqueAcquisition(Player player) {
        RealmChangeEvent event = new RealmChangeEvent.Post(player,getPath(),-1,0,-1,0,0,null);
        updatePlayerSkills(event);
    }

    public AbstractTechnique setDescription(Component description){
        this.description = description;
        return this;
    }

    @Override
    public Component getDescription() {
        return description;
    }

    @Override
    public List<Label> getDisplayDaoEfficiencies(IEasyGuiScreen screen) {
        List<Label> extraInfo = new ArrayList<>();
        extraInfo.add(
                (new Label.Builder())
                        .screen(screen)
                        .x(0).y(0)
                        .text(Component.literal("Dao Efficiencies:").withStyle(ChatFormatting.BOLD))
                        .customScaling(0.5)
                        .build()
        );
        for (Map.Entry<String ,Double> value : daoBonuses.entrySet()){
            Component text = AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(value.getKey(),':')).getDisplayTitle().copy().append(": "+value.getValue().toString());
            HoverableLabel hoverableLabel = (new HoverableLabel.Builder())
                    .screen(screen)
                    .x(0).y(0)
                    .text(text)
                    .customScaling(0.5)
                    .dao(AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(value.getKey(),':')))
                    .build();
            extraInfo.add(hoverableLabel);
        }
        return extraInfo;
    }
    public void updatePlayerSkills(RealmChangeEvent event){
        System.out.println("trying to update player skills");
        System.out.println("major : "+event.oldMajorRealm +"->"+event.newMajorRealm);
        System.out.println("minor : "+event.oldMinorRealm +"->"+event.newMinorRealm);
        getSkillList().onRealmChange(event);
    }


    public AbstractTechnique setDataHandler(Supplier<ITechniqueData> dataSupplier){this.dataSupplier = dataSupplier;return this;}

    @Override
    public ITechniqueData getTechniqueDataInstance() {
        return dataSupplier.get();
    }

    @Override
    public ITechniqueData getTechniqueDataInstance(CompoundTag tag) {
        ITechniqueData data = getTechniqueDataInstance();
        data.readData(tag);
        return data;
    }

    @Override
    public ITechniqueData getTechniqueDataInstance(RegistryFriendlyByteBuf buf) {
        ITechniqueData data = getTechniqueDataInstance();
        data.decode(buf);
        return data;
    }

    @Override
    public double getQiForRealm(int majorRealm, int minorRealm) {
        return 1000*(getMinorRealmMultiplier(minorRealm,majorRealm)+getMajorRealmMultiplier(majorRealm)) ;
    }
    private double getMinorRealmMultiplier(int minorRealm,int majorRealm){
        double total = 0;
        for(int i = 0; i<minorRealm+majorRealm*9;i++){
            total += 2.47+0.06*i;
        }
        return total;
    }
    private double getMajorRealmMultiplier(int majorRealm){
        double total = 0.0;
        for(int i = 0;i<majorRealm;i++){
            total += 2+i;
        }
        return total;
    }
}
