package net.thejadeproject.ascension.sects.missions;

import net.minecraft.nbt.CompoundTag;

public class MissionRequirement {
    public enum RequirementType {
        ITEM, KILL_MOB
    }

    private final RequirementType type;
    private final String target;
    private final int count;

    public MissionRequirement(RequirementType type, String target, int count) {
        this.type = type;
        this.target = target;
        this.count = count;
    }

    public RequirementType getType() { return type; }
    public String getTarget() { return target; }
    public int getCount() { return count; }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", type.name());
        tag.putString("target", target);
        tag.putInt("count", count);
        return tag;
    }

    public static MissionRequirement fromNBT(CompoundTag tag) {
        RequirementType type = RequirementType.valueOf(tag.getString("type"));
        String target = tag.getString("target");
        int count = tag.getInt("count");
        return new MissionRequirement(type, target, count);
    }
}
