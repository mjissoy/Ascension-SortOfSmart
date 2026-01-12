package net.thejadeproject.ascension.progression.realms;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.constants.LivingEntityState;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GenericPath implements IPath {

    private final ResourceLocation pathId;
    private final Component displayName;
    private final List<Component> majorRealms;
    private Set<String> states = Set.of(LivingEntityState.PHYSICAL);
    public GenericPath(ResourceLocation pathId, Component displayName, List<Component> majorRealms){
        this.pathId = pathId;
        this.displayName = displayName;
        this.majorRealms = majorRealms;
    }
    public GenericPath setPathLivingEntityStates(Set<String> states){
        this.states = states;
        return this;
    }
    @Override
    public ResourceLocation getPathId() {
        return pathId;
    }

    @Override
    public Component getPathDisplayName() {
        return displayName;
    }

    @Override
    public Component getMajorRealmName(int majorRealm) {
        return majorRealms.get(majorRealm);
    }

    @Override
    public Component getMinorRealmName(int majorRealm, int minorRealm) {
        return Component.literal(String.valueOf(minorRealm));
    }

    @Override
    public int getMaxMajorRealm() {
        return majorRealms.size();
    }

    @Override
    public int getMaxMinorRealm(int majorRealm) {
        return 9;
    }

    @Override
    public Set<String> getPathLivingEntityStates() {
        return states;
    }
}
