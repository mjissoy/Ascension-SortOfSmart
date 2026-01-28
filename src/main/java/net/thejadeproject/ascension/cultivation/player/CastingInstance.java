package net.thejadeproject.ascension.cultivation.player;

import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.nio.charset.Charset;
import java.util.UUID;

//built like this so i can have multiple casting instances
public class CastingInstance {
    public CastType castType;
    public ICastData castData;// additional info the spell might need
    public int castTickElapsed = 0;
    public ResourceLocation skillId;
    public UUID uuid; //for casting threads

    public boolean isReal = true; //for syncing
    public CastingInstance(ISkill skill,UUID uuid){
        castType = ((AbstractActiveSkill)skill).getCastType();
        this.uuid = uuid;
        skillId = AscensionRegistries.Skills.SKILL_REGISTRY.getKey(skill);
    }

    public CastingInstance(UUID uuid,ResourceLocation skillId,CastType castType){
        this.castType = castType;
        this.uuid = uuid;
        this.skillId = skillId;
    }
    public void cancelCast(){
        //TODO
    }


    public boolean tick(Level level, Player player){
        AbstractActiveSkill activeSkill = (AbstractActiveSkill) AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId);
        if(activeSkill == null) return false;
        boolean result = activeSkill.continueCasting(castTickElapsed,level,player,castData);
        castTickElapsed += 1;
        return result;
    }


    public static void encode(ByteBuf buffer,CastingInstance instance){
        buffer.writeBoolean(instance.isReal);//for if it exists or not
        if(!instance.isReal) return;
        buffer.writeInt(instance.castType.name().length());
        buffer.writeCharSequence(instance.castType.name(), Charset.defaultCharset());
        buffer.writeInt(instance.castTickElapsed);
        buffer.writeInt(instance.skillId.toString().length());
        buffer.writeCharSequence(instance.skillId.toString(),Charset.defaultCharset());
        buffer.writeInt(instance.uuid.toString().length());
        buffer.writeCharSequence(instance.uuid.toString(),Charset.defaultCharset());
    }

    public static CastingInstance decode(ByteBuf buffer){
        if(!buffer.readBoolean()) return null;
        CastType castType = CastType.valueOf((String) buffer.readCharSequence(buffer.readInt(),Charset.defaultCharset()));
        int ticksElapsed = buffer.readInt();
        ResourceLocation resourceLocation = ResourceLocation.bySeparator(
                (String) buffer.readCharSequence(buffer.readInt(),Charset.defaultCharset()),':'
        );
        UUID uuid =UUID.fromString((String) buffer.readCharSequence(buffer.readInt(),Charset.defaultCharset()));
        CastingInstance instance = new CastingInstance(uuid,resourceLocation,castType);
        instance.castTickElapsed = ticksElapsed;
        return instance;
    }
}
