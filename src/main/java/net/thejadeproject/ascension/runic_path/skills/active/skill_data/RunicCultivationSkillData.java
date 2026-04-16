package net.thejadeproject.ascension.runic_path.skills.active.skill_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;

public class RunicCultivationSkillData implements IPersistentSkillData {

    private double baseRate;
    private double totalRunesDrawn;

    public RunicCultivationSkillData(double baseRate) {
        this.baseRate = baseRate;
        this.totalRunesDrawn = 0;
    }

    public RunicCultivationSkillData(CompoundTag tag) {
        this.baseRate = tag.getDouble("base_rate");
        this.totalRunesDrawn = tag.getDouble("total_runes_drawn");
    }

    public RunicCultivationSkillData(RegistryFriendlyByteBuf buf) {
        this.baseRate = buf.readDouble();
        this.totalRunesDrawn = buf.readDouble();
    }

    public double getBaseRate() {
        return baseRate;
    }

    public double getTotalRunesDrawn() {
        return totalRunesDrawn;
    }

    public void addRunesDrawn(double amount) {
        this.totalRunesDrawn += amount;
    }

    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("base_rate", baseRate);
        tag.putDouble("total_runes_drawn", totalRunesDrawn);
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeDouble(baseRate);
        buf.writeDouble(totalRunesDrawn);
    }
}