package net.thejadeproject.ascension.refactor_packages.paths.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Panda;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.IPath;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.paths.PathInteraction;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenericPath implements IPath {
    private final Component title;
    private Component pathDescription;
    private HashMap<ResourceLocation, Double> destructive = new HashMap<>();
    private HashMap<ResourceLocation, Double> generative = new HashMap<>();
    private HashMap<ResourceLocation, Double> related = new HashMap<>();
    private ArrayList<Component> realmNames = new ArrayList<>();
    public GenericPath(Component title){
        this.title = title;
    }
    public GenericPath addDestructiveInteraction(ResourceLocation path, double val){
        destructive.put(path,val);
        return this;
    }
    public GenericPath addGenerativeInteraction(ResourceLocation path, double val){
        generative.put(path,val);
        return this;
    }
    public GenericPath addRelatedInteraction(ResourceLocation path, double val){
        related.put(path,val);
        return this;
    }
    public GenericPath setDescription(Component description){
        this.pathDescription = description;
        return this;
    }
    public GenericPath addMajorRealmName(Component realmName){
        realmNames.add(realmName);
        return this;
    }
    public GenericPath addMajorRealmName(String realmName){
        realmNames.add(Component.literal(realmName));
        return this;
    }

    @Override
    public Component getDisplayTitle() {
        return title;
    }

    @Override
    public Component getDescription() {
        return pathDescription;
    }

    @Override
    public Component getDisplayPathInteractions() {
        return Component.empty();//TODO
    }

    @Override
    public Component getMajorRealmName(int majorRealm) {
        return realmNames.size()>=majorRealm ? Component.literal(String.valueOf(majorRealm)) : realmNames.get(majorRealm);
    }

    @Override
    public Component getMinorRealmName(int majorRealm, int minorRealm) {
        return Component.literal(String.valueOf(minorRealm));
    }


    @Override
    public int getMaxMajorRealm() {
        return 5;
    }

    @Override
    public int getMaxMinorRealm(int majorRealm) {
        return 9;
    }

    @Override
    public int getMaxQiForRealm(int majorRealm, int minorRealm) {
        return 100;
    }


    @Override
    public Double getInteractionValue(ResourceLocation path) {
        return 0.0;
    }

    @Override
    public PathInteraction getInteractionType(ResourceLocation path) {
        return null;
    }

    @Override
    public ResourceLocation defaultForm() {
        return null;
    }

    @Override
    public PathData freshPathData(IEntityData heldEntity) {
        return new PathData(AscensionRegistries.Paths.PATHS_REGISTRY.getKey(this));
    }

    @Override
    public PathData fromCompound(CompoundTag tag, IEntityData heldEntity) {
        //todo handle cultivation data simulations
        return null;
    }

    @Override
    public PathData fromNetwork(RegistryFriendlyByteBuf buf, IEntityData heldEntity) {
        //TODO
        return null;
    }
}
