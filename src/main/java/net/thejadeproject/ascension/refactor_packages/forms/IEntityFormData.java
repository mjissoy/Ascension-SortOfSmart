package net.thejadeproject.ascension.refactor_packages.forms;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.attributes.AscensionAttributesContainer;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodlineData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.player_data.PlayerData;
import net.thejadeproject.ascension.refactor_packages.skills.HeldSkills;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.StatSheet;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IEntityFormData extends IDataInstance {

    UUID getAttachedEntity();
    void setAttachedEntity(UUID entity);
    ResourceLocation getEntityFormId();
    IEntityForm getEntityForm();





    PathData getPathData(ResourceLocation path);
    Collection<PathData> getAllPathData();

    boolean hasPathData(ResourceLocation path);

    HeldSkills getHeldSkills();


    // i want to be able to sync stuff separate for more efficient network traffic
    IPhysiqueData getPhysiqueData();
    void setPhysiqueData(IPhysiqueData data);
    IBloodlineData getBloodlineData();
    void setBloodlineData(IBloodlineData data);



    StatSheet getStatSheet();



}
