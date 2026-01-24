package net.thejadeproject.ascension.formations.formation_nodes;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.formation_arrays.api.capability.Capabilities;
import net.lucent.formation_arrays.api.capability.IAccessControlToken;
import net.lucent.formation_arrays.api.formations.IFormation;
import net.lucent.formation_arrays.blocks.block_entities.formation_cores.AbstractFormationCoreBlockEntity;
import net.lucent.formation_arrays.formations.node.FormationNode;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.capabilities.AscensionCapabilities;
import net.thejadeproject.ascension.capabilities.IPlayerFilter;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.entity.custom.formation.DummyEntity;
import net.thejadeproject.ascension.formations.IDummyListenerNode;
import net.thejadeproject.ascension.network.serverBound.PlayerTryHitBarrier;
import net.thejadeproject.ascension.util.math.BoundCheckHelpers;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BarrierFormation extends FormationNode implements IDummyListenerNode {

    public final int BARRIER_RADIUS;
    public final double BARRIER_MAX_HEALTH;
    public final int TICKS_TO_RESPAWN;
    public final double HEALTH_REGEN_RATE;
    public final int BASE_QI_DRAIN;
    public final int HEALING_QI_DRAIN;
    public final int BARRIER_COLOUR = 1677739263;

    public double currentBarrierHealth;
    public boolean isDestroyed;
    public int respawnTicks;

    public BlockPos currentLocation;
    public LivingEntity dummyEntity;
    boolean lookingAtEntity; //clientThing
    public BarrierFormation(IFormation formation, UUID uuid, int barrierRadius, double barrierMaxHealth, int ticksToRespawn, double healthRegenRate, int baseQiDrain, int healingQiDrain) {
        super(formation);
        setFormationId(uuid);
        BARRIER_RADIUS = barrierRadius;
        BARRIER_MAX_HEALTH = barrierMaxHealth;
        TICKS_TO_RESPAWN = ticksToRespawn;
        HEALTH_REGEN_RATE = healthRegenRate;
        BASE_QI_DRAIN = baseQiDrain;
        HEALING_QI_DRAIN = healingQiDrain;
        currentBarrierHealth = BARRIER_MAX_HEALTH;
        NeoForge.EVENT_BUS.addListener(this::onBlockPlace);
        NeoForge.EVENT_BUS.addListener(this::onDetonation);
        NeoForge.EVENT_BUS.addListener(this::onBlockBreak);
        NeoForge.EVENT_BUS.addListener(this::onInput);
        NeoForge.EVENT_BUS.addListener(this::damageListener);
        NeoForge.EVENT_BUS.addListener(this::onMobGrief);
    }

    //TODO modify all 3 so it checks if the player has permission

    public void onDetonation(ExplosionEvent.Detonate event){
        if(!activeLastTick()) return;
        if(event.getExplosion().center().distanceToSqr(currentLocation.getCenter()) < BARRIER_RADIUS*BARRIER_RADIUS) return;
        event.getAffectedBlocks().removeIf(pos -> pos.getCenter().distanceToSqr(currentLocation.getCenter()) < BARRIER_RADIUS * BARRIER_RADIUS);
        event.getAffectedEntities().removeIf(entity -> entity.position().distanceToSqr(currentLocation.getCenter()) < BARRIER_RADIUS * BARRIER_RADIUS);

        //TODO process explosion damage on barrier
    }
    public void onBlockBreak(BlockEvent.BreakEvent event){
        if(!activeLastTick()) return; // barrier is off
        if(event.getPos().distSqr(currentLocation) > BARRIER_RADIUS*BARRIER_RADIUS) return;
        boolean placedInside = event.getPos().distSqr(currentLocation) < BARRIER_RADIUS*BARRIER_RADIUS;
        boolean result = BoundCheckHelpers.doesSphereOverlapBoundingBox(currentLocation.getCenter(),BARRIER_RADIUS,event.getPlayer().getBoundingBox());
        if(result){
            Vec3 collisionCorner = BoundCheckHelpers.getSphereBoundingBoxCollisionCorner(currentLocation.getCenter(),BARRIER_RADIUS,event.getPlayer().getBoundingBox());
            if(collisionCorner.distanceToSqr(currentLocation.getCenter()) > BARRIER_RADIUS*BARRIER_RADIUS - 50 && placedInside) event.setCanceled(true);
        }else{
            if(placedInside) event.setCanceled(true);
        }

    }
    public void onMobGrief(EntityMobGriefingEvent event){
        if(!activeLastTick()) return;
        if(event.getEntity().position().distanceToSqr(currentLocation.getCenter()) < BARRIER_RADIUS*BARRIER_RADIUS){
            event.setCanGrief(false);
        }
    }
    public void onInput(InputEvent.InteractionKeyMappingTriggered event){
        if(event.isAttack()){
            if(lookingAtEntity){

                    PacketDistributor.sendToServer(new PlayerTryHitBarrier(getFormationId().toString(),currentLocation));

            }
        }
    }
    public void damageListener(LivingDamageEvent.Pre event){
        if(event.getEntity() != null){
            System.out.println("DETTECTED DAMAGE");
            System.out.println(event.getEntity().getType());
        }
    }
    public void handlePlayerAttackAttempt(Player player){
        System.out.println("PLAYER TRIED ATTACKING THE MF BARRIER BABY");
    }
    //TODO UPDATE TO INCLUDE BLOCK DETECTION SO I DON'T ACCIDENTALY TARGET THROUGH BLOCKS
    //TODO UPDATE TO CHECK RANGE SO I DON'T TARGET THE BARRIER FROM INSIDE WITHOUT ACTUALY LOOKING AT IT
    //TODO TO MAKE MY LIFE EASIER IGNORE MOST RAYCAST SHIT, JUST CHECK IF THEY ARE LOOKING AT BARRIER THEN CEHCK
    //TODO IF hit != null
    public void checkPlayerTargeting(AbstractFormationCoreBlockEntity blockEntity){
        if(!activeLastTick()) return;
        lookingAtEntity = false;
        Minecraft mc = Minecraft.getInstance();
        mc.getProfiler().push("pick");
        Player player = mc.player;
        double range = mc.player.entityInteractionRange();
        float partialTicks = mc.getFrameTimeNs();

        double d1 = Mth.square(range);
        Vec3 vec3 = player.getEyePosition(partialTicks);
        HitResult hitresult = player.pick(range, partialTicks, false);
        double d2 = hitresult.getLocation().distanceToSqr(vec3);
        if (hitresult.getType() != HitResult.Type.MISS) {
            d1 = d2;
            range = Math.sqrt(d2);
        }

        Vec3 vec31 = player.getViewVector(partialTicks);
        Vec3 vec32 = vec3.add(vec31.x * range, vec31.y * range, vec31.z * range);
        float f = 1.0F;
        AABB aabb = player.getBoundingBox().expandTowards(vec31.scale(range)).inflate(1.0, 1.0, 1.0);
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(
                player, vec3, vec32, aabb, p_234237_ -> !p_234237_.isSpectator() && p_234237_.isPickable(), d1
        );
        Vec3 newPoint = blockEntity.getBlockPos().getCenter().add(
                vec3.subtract(blockEntity.getBlockPos().getCenter()).normalize().scale(BARRIER_RADIUS)
        );
        if(newPoint.distanceToSqr(blockEntity.getBlockPos().getCenter()) < BARRIER_RADIUS*BARRIER_RADIUS - 27) return; // the point they are looking at is past the barrier and inside it

        boolean doesOverlap = BoundCheckHelpers.doesSphereOverlapBoundingBox(blockEntity.getBlockPos().getCenter(),BARRIER_RADIUS,aabb);
        if(entityhitresult != null && doesOverlap){
           double distance1 =  entityhitresult.getLocation().distanceToSqr(player.position());
           double distance2 = vec3.distanceToSqr(blockEntity.getBlockPos().getCenter())-(BARRIER_RADIUS*BARRIER_RADIUS);
           if(distance2< distance1){
               entityhitresult = new EntityHitResult(dummyEntity,newPoint);
               dummyEntity.setPos(newPoint);
               lookingAtEntity = true;
           }
        }else if(doesOverlap) {
            entityhitresult = new EntityHitResult(dummyEntity,newPoint);
            dummyEntity.setPos(newPoint);
            lookingAtEntity = true;
        }

        if (doesOverlap){
            System.out.println("PLAYER LOOKING AT ME!!!");
            System.out.println(entityhitresult.getEntity().getType().toString());
        }

        //barrier is not the closes thing to the player
        if(mc.hitResult != null && vec3.distanceToSqr(mc.hitResult.getLocation()) < vec3.distanceToSqr(newPoint)) return;

        if(entityhitresult != null && entityhitresult.getLocation().distanceToSqr(vec3) < d2)
        {
            mc.hitResult = entityhitresult;
            mc.crosshairPickEntity = entityhitresult instanceof EntityHitResult newHitResult ? newHitResult.getEntity() : null;

        }
        mc.getProfiler().pop();

    }
    //temporarily ignore these, need to find a better way to force hit my entity
    public void handleProjectile(Projectile projectile,AbstractFormationCoreBlockEntity blockEntity, List<Pair<IPlayerFilter,ItemStack>> filters){

    }

    public void spawnDummyEntity(BlockPos core, Level level){
        this.dummyEntity = new DummyEntity(ModEntities.DUMMY_ENTITY.get(),level,this);
        dummyEntity.setPos(core.getCenter());
        level.addFreshEntity(dummyEntity);
    };
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event){
        if(!activeLastTick()) return; // barrier is off
        if(event.getPos().distSqr(currentLocation) > BARRIER_RADIUS*BARRIER_RADIUS) return;
        boolean placedInside = event.getPos().distSqr(currentLocation) < BARRIER_RADIUS*BARRIER_RADIUS;
        if(event.getEntity() == null){
            if( placedInside) event.setCanceled(true);
            return;
        }

        boolean result = BoundCheckHelpers.doesSphereOverlapBoundingBox(currentLocation.getCenter(),BARRIER_RADIUS,event.getEntity().getBoundingBox());
        if(result){
            Vec3 collisionCorner = BoundCheckHelpers.getSphereBoundingBoxCollisionCorner(currentLocation.getCenter(),BARRIER_RADIUS,event.getEntity().getBoundingBox());
            if(collisionCorner.distanceToSqr(currentLocation.getCenter()) > BARRIER_RADIUS*BARRIER_RADIUS - 50 && placedInside) event.setCanceled(true);
        }else{
            if(placedInside) event.setCanceled(true);
        }
    }

    @Override
    public void deactivate(AbstractFormationCoreBlockEntity blockEntity) {
        super.deactivate(blockEntity);
        if(dummyEntity != null) dummyEntity.remove(Entity.RemovalReason.DISCARDED);
    }

    public void tryRecover(AbstractFormationCoreBlockEntity blockEntity){
        currentBarrierHealth = Math.min(BARRIER_MAX_HEALTH,currentBarrierHealth + HEALTH_REGEN_RATE);
        respawnTicks -= 1;
        if(respawnTicks <= 0){
            isDestroyed=false;
        }
    }


    @Override
    public void tick(AbstractFormationCoreBlockEntity blockEntity,List<ItemStack> jades) {
        super.tick(blockEntity,jades);
        if(blockEntity.getLevel().isClientSide()) checkPlayerTargeting(blockEntity);
        if(isDestroyed){
            tryRecover(blockEntity);
            return;
        }
        currentLocation = blockEntity.getBlockPos();
        collisionCheck(blockEntity,jades);
        if(dummyEntity == null) spawnDummyEntity(blockEntity.getBlockPos(),blockEntity.getLevel());
    }


    public void collisionCheck(AbstractFormationCoreBlockEntity blockEntity,List<ItemStack> jades){
        Level level = blockEntity.getLevel();
        List<Projectile> projectiles = new ArrayList<>();
        List<Entity> entities = level.getEntities((Entity) null,(new AABB(blockEntity.getBlockPos())).inflate(BARRIER_RADIUS), entity ->{
            if(entity instanceof Projectile projectile) {
                //TODO perform early damage calculations for this entity
                //TODO only perform calcs if coming from outside
                projectiles.add(projectile);

            }
            boolean result = BoundCheckHelpers.doesSphereOverlapBoundingBox(blockEntity.getBlockPos().getCenter(),BARRIER_RADIUS,entity.getBoundingBox());
            if(result){
                //It does overlap
                Vec3 collisionCorner = BoundCheckHelpers.getSphereBoundingBoxCollisionCorner(blockEntity.getBlockPos().getCenter(),BARRIER_RADIUS,entity.getBoundingBox());

                return collisionCorner.distanceToSqr(blockEntity.getBlockPos().getCenter()) > BARRIER_RADIUS*BARRIER_RADIUS - 50;
                 //giving them some leeway
            }
            return false;


        });
        List<Pair<IPlayerFilter,ItemStack>> filters = new ArrayList<>();
        for(ItemStack stack : jades){
            IPlayerFilter filter = stack.getCapability(AscensionCapabilities.PLAYER_FILTER_CAPABILITY);
            if(filter != null) filters.add(new Pair<>(filter,stack));
        }
        //push all out for now
        ItemStack controlToken =  blockEntity.getFormationItemStackHandler().getControlToken();
        IAccessControlToken token = controlToken.getCapability(Capabilities.ACCESS_TOKEN_CAPABILITY);
        for(Entity entity : entities){

            if(entity instanceof Player player){
                if(token != null && token.hasPermission(player,controlToken)) continue;
                if(checkFilters(player,filters)) continue;
            }
            System.out.println("trying to block entity : "+ entity.getDisplayName().getString());
            blockEntityMotion(entity,blockEntity.getBlockPos().getCenter());

        }
    }
    public boolean checkFilters(Player player,  List<Pair<IPlayerFilter,ItemStack>> filters ){
        for (Pair<IPlayerFilter,ItemStack> filter: filters){
            if(filter.getA().filterPlayer(player,filter.getB())) return true;
        }
        return false;
    }

    public void blockEntityMotion(Entity entity, Vec3 position){

        Vec3 entityPos = entity.position();
        Vec3 reactionForce = new Vec3(
                entityPos.x - position.x(),
                entityPos.y - position.y(),
                entityPos.z - position.z()
        );
        reactionForce = reactionForce.normalize().scale(0.5);
        entity.setDeltaMovement(reactionForce);
        if(entity instanceof ServerPlayer player){
            player.connection.send(new ClientboundSetEntityMotionPacket(entity));
        }
    }
    @Override
    public int getEnergyCost() {
        return BASE_QI_DRAIN + (isDestroyed ? HEALING_QI_DRAIN : 0);
    }

    public void barrierDestroyed(){
        this.isDestroyed = true;
        this.respawnTicks = this.TICKS_TO_RESPAWN;
    }

    @Override
    public void onHurt(DamageSource source, float amount) {
        this.currentBarrierHealth = Math.max(this.currentBarrierHealth,this.currentBarrierHealth-amount);
        if(currentBarrierHealth == 0) barrierDestroyed();
    }

    @Override
    public CompoundTag save(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("current_health",currentBarrierHealth);
        tag.putBoolean("is_destroyed",isDestroyed);
        tag.putInt("respawn_ticks",respawnTicks);
        return tag;
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries) {
        currentBarrierHealth = tag.getDouble("current_health");
        isDestroyed = tag.getBoolean("is_destroyed");
        respawnTicks = tag.getInt("respawn_ticks");
        super.read(tag, registries);
    }
}
