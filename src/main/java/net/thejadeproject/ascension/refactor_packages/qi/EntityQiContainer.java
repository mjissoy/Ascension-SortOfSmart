package net.thejadeproject.ascension.refactor_packages.qi;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.IPath;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttributes;

import java.util.HashMap;

/**
 * Holds the qi of an entity, the max amount is based of the MAX QI attribute
 *
 * however internally it is handled a bit different,the players qi is separated into "sections"
 * any leftover is assumed to be attributed qi.
 *
 * when trying to regen qi we use the tryRegen method in paths, and after consuming qi we call qiConsumed
 * this allows for more custom behaviour. path also has a conversion rate e.g 1 fire qi = 2 qi. so if a player has
 * max 100 qi, after conversion they have 50 fire qi
 *
 * when trying to use qi you can either specify the qi to use. OR you can not specify in which case it consumes it randomly
 *
 *
 */
public class EntityQiContainer {

    public static class QiSegment{
        private double MAX;
        private double current;
        private final ResourceLocation path;
        public QiSegment(ResourceLocation path,double max) {
            this.MAX = max;
            this.current = max;
            this.path = path;
        }
        public double getMaxPureQiEquivalent(){
            return AscensionRegistries.Paths.PATHS_REGISTRY.get(path).getQiConversionRatio()*MAX;
        }
        public double getPureQiEquivalent(){
            return AscensionRegistries.Paths.PATHS_REGISTRY.get(path).getQiConversionRatio()*current;
        }
        public void addConvertedQi(double amount){
            MAX += amount;
            current += amount;
        }
        public void setCurrentQi(double amount){

        }
        public double getCurrentQi(){return current;}
        public double getMaxQi(){return MAX;}
    }


    public static final ResourceLocation PURE_QI = ModPaths.ESSENCE.getId();
    private double currentPureQi;
    private IEntityData attachedEntity;


    private HashMap<ResourceLocation,QiSegment> segmentedQi = new HashMap<>();

    public EntityQiContainer(IEntityData attachedEntity){
        this.attachedEntity = attachedEntity;

    }
    public void fullFillQi(){
        for(QiSegment segment : segmentedQi.values()){
            segment.setCurrentQi(segment.getMaxQi());
        }
        currentPureQi = getMaxPureQi();
    }
    public double getMaxPureQi(){
        double maxQi = attachedEntity.getAscensionAttributeHolder().getAttribute(ModAttributes.MAX_QI).getValue();
        for(QiSegment segment : segmentedQi.values()){
            maxQi -= segment.getPureQiEquivalent();
        }
        return maxQi;
    }
    public double getPureQiEquivalent(ResourceLocation path,double amount){
        return AscensionRegistries.Paths.PATHS_REGISTRY.get(path).getQiConversionRatio()*amount;
    }
    public boolean tryConvertQi(ResourceLocation path,double amount){
        double maxQi = getMaxPureQi();
        if(maxQi<getPureQiEquivalent(path,amount)) return false;

        if(segmentedQi.containsKey(path))segmentedQi.get(path).addConvertedQi(amount);
        else segmentedQi.put(path,new QiSegment(path,amount));

        currentPureQi = Math.max(0,currentPureQi-getPureQiEquivalent(path,amount));
        return true;
    }

    public void tryRegenQi(){
        double regenRate = attachedEntity.getAscensionAttributeHolder().getAttribute(ModAttributes.QI_REGEN_RATE).getValue();
        for(QiSegment segment : segmentedQi.values()){

            IPath path = AscensionRegistries.Paths.PATHS_REGISTRY.get(segment.path);
            double regenAmount = regenRate
                    *(1/path.getQiConversionRatio())
                    *attachedEntity.getPathBonusHandler().getPathBonus(segment.path);
            regenAmount = path.tryRegenQi(regenAmount,attachedEntity);
            segment.setCurrentQi(Math.min(segment.current+regenAmount,segment.MAX));

        }
        IPath essence = AscensionRegistries.Paths.PATHS_REGISTRY.get(PURE_QI);
        double regenAmount = regenRate*attachedEntity.getPathBonusHandler().getPathBonus(PURE_QI);
        currentPureQi = Math.min(regenAmount+currentPureQi, getMaxPureQi());
    }

    public void tryConsumeQi(double amount){
        //TODO
    }
    public boolean tryConsumeQi(ResourceLocation path,double amount){
        if(!path.equals(PURE_QI)){
            if(!segmentedQi.containsKey(path)) return false;
            if(segmentedQi.get(path).current < amount)return false;
            segmentedQi.get(path).current -= amount;
        }else{
            if(currentPureQi < amount) return false;
            currentPureQi -= amount;
        }
        AscensionRegistries.Paths.PATHS_REGISTRY.get(path).qiConsumed(amount,attachedEntity);
        return true;
    }
    public void reduceMaxConvertedQi(ResourceLocation path,double amount){
        if(!segmentedQi.containsKey(path)) return;

        segmentedQi.get(path).addConvertedQi(amount);
        if(segmentedQi.get(path).getMaxQi() <=0) segmentedQi.remove(path);
    }

    public double getCurrentQi(ResourceLocation path){
        if(!path.equals(PURE_QI) && segmentedQi.containsKey(path)){
            return segmentedQi.get(path).getCurrentQi();
        }
        return currentPureQi;
    }
    public double getMaxQi(ResourceLocation path){
        if(!path.equals(PURE_QI) && segmentedQi.containsKey(path)){
            return segmentedQi.get(path).getMaxQi();
        }
        return getMaxPureQi();
    }


}
