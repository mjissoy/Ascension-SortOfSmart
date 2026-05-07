package net.thejadeproject.ascension.refactor_packages.runic.casting;

import net.minecraft.resources.ResourceLocation;

public class RunicCastingResult {

    private final boolean success;
    private final ResourceLocation sequenceId;
    private final String failureReason;

    private RunicCastingResult(boolean success, ResourceLocation sequenceId, String failureReason) {
        this.success = success;
        this.sequenceId = sequenceId;
        this.failureReason = failureReason;
    }

    public static RunicCastingResult success(ResourceLocation sequenceId) {
        return new RunicCastingResult(true, sequenceId, "");
    }

    public static RunicCastingResult failure(String failureReason) {
        return new RunicCastingResult(false, null, failureReason);
    }

    public boolean isSuccess() {
        return success;
    }

    public ResourceLocation getSequenceId() {
        return sequenceId;
    }

    public String getFailureReason() {
        return failureReason;
    }
}