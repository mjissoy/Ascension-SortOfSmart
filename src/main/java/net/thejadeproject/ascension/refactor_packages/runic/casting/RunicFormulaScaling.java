package net.thejadeproject.ascension.refactor_packages.runic.casting;

import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;

public final class RunicFormulaScaling {

    private RunicFormulaScaling() {
    }

    public static RunicFormulaStats calculate(RunicFormula formula, LivingEntity caster, int runicRealm) {
        float intelligence = getStat(caster, ModStats.INTELLIGENCE.get());
        float strength = getStat(caster, ModStats.STRENGTH.get());
        float agility = getStat(caster, ModStats.AGILITY.get());
        float vitality = getStat(caster, ModStats.VITALITY.get());

        float damage = 1.0F + (runicRealm * 0.35F);
        float duration = 1.0F + (runicRealm * 0.15F);
        float range = 1.0F + (runicRealm * 0.10F);

        damage += intelligence * 0.025F;

        if (formula.intentPath().equals("cut") || formula.intentPath().equals("pierce")) {
            damage += strength * 0.012F;
        }

        if (formula.sourcePath().equals("wind")
                || formula.sourcePath().equals("lightning")
                || formula.hasModifier("quicken")) {
            damage += agility * 0.008F;
            range += agility * 0.003F;
        }

        if (formula.intentPath().equals("guard")
                || formula.intentPath().equals("heal")
                || formula.formPath().equals("veil")) {
            duration += vitality * 0.008F;
        }

        float qiCost = 1.0F + (formula.inputRunes().size() * 0.22F);
        float backlash = 1.0F;

        if (formula.hasModifier("violent")) {
            damage += 0.45F;
            backlash += 0.7F;
            qiCost += 0.35F;
        }

        if (formula.hasModifier("heavy")) {
            duration += 0.25F;
            qiCost += 0.2F;
        }

        if (formula.hasModifier("quicken")) {
            duration -= 0.2F;
            qiCost += 0.1F;
        }

        if (formula.hasModifier("stabilise")) {
            backlash -= 0.4F;
            damage -= 0.15F;
        }

        if (formula.hasModifier("hidden")) {
            damage -= 0.1F;
            qiCost += 0.15F;
        }

        return new RunicFormulaStats(
                Math.max(0.5F, damage),
                Math.max(0.5F, duration),
                Math.max(0.5F, range),
                Math.max(0.5F, qiCost),
                Math.max(0.2F, backlash)
        );
    }

    private static float getStat(LivingEntity entity, Stat stat) {
        if (entity == null || !entity.hasData(ModAttachments.ENTITY_DATA)) {
            return 0.0F;
        }

        IEntityData entityData = entity.getData(ModAttachments.ENTITY_DATA);

        if (entityData.getActiveFormData() == null || entityData.getActiveFormData().getStatSheet() == null) {
            return 0.0F;
        }

        var statInstance = entityData.getActiveFormData().getStatSheet().getStatInstance(stat);

        if (statInstance == null) {
            return 0.0F;
        }

        return (float) statInstance.getValue();
    }
}