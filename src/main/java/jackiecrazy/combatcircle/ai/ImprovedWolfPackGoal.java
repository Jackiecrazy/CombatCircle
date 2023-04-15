package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.combatcircle.utils.GoalUtils;
import jackiecrazy.footwork.api.FootworkAttributes;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.EnumSet;

/**
 * this goal causes mobs to factor in other mobs and try to move away from them when near the player, unless they are attacking
 */
public class ImprovedWolfPackGoal extends Goal {

    static final EnumSet<Flag> mutex = EnumSet.allOf(Flag.class);
    protected final PathNavigation pathNav;
    protected final double slightSpread;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    private int restartPathingDelay=0;
    protected PathfinderMob mob;
    private final TargetingConditions SAME_TARGET = TargetingConditions.forNonCombat().selector(from -> {
        if (!(from instanceof Mob && mob != null)) return false;
        if (((Mob) from).getTarget() == null) return false;
        return ((Mob) from).getTarget() == mob.getTarget();
    });
    protected Path path;
    private static final Vec3[] ALTERNATIVES={new Vec3(0,1,0), new Vec3(0,-1,0)};

    public ImprovedWolfPackGoal(PathfinderMob entityIn) {
        super();
        mob = entityIn;
        walkSpeedModifier = 0.8f;
        sprintSpeedModifier = 1.2f;
        slightSpread = Math.random();
        pathNav = entityIn.getNavigation();
    }

    public boolean canUse() {
        final LivingEntity target = mob.getTarget();
        if (target == null) return false;
        //too far away, just charge in
        //if (target.distanceToSqr(mob) > CombatCircle.SPREAD_DISTANCE * CombatCircle.SPREAD_DISTANCE * 4) return false;
        //becomes an attacker, moves as they please
        if (CombatManager.getManagerFor(target).addAttacker(mob, null)) return false;

        //determine safe distance from player
        double safeDist = Math.max(GeneralUtils.getAttributeValueSafe(target, ForgeMod.ATTACK_RANGE.get()), GeneralUtils.getAttributeValueSafe(mob, FootworkAttributes.ENCIRCLEMENT_DISTANCE.get())) + slightSpread;
        //start with random base vector to emulate natural strafe
        Vec3 toAvoid = new Vec3(Math.random(), Math.random(), Math.random());
        //try to maintain distance from others in a close-ish radius
        for (Entity other : mob.level.getNearbyEntities(LivingEntity.class, SAME_TARGET, mob, mob.getBoundingBox().inflate(safeDist * 2))) {
            //determine if there are same target mobs too close
            if (other instanceof Mob mob2 && target == mob2.getTarget() && other != target) {
                //add to avoid vector
                Vec3 diff = mob.position().subtract(other.position());
                toAvoid = toAvoid.add(diff.normalize().scale(other.getBbWidth()));
            }
        }
        //normalize to reduce vector influence, more influence is exerted closer to the player
        toAvoid = toAvoid.normalize();
        //move to
        Vec3 targetToMob=mob.position().subtract(target.position());

//        //consider inversion
//        Vec3 alt=targetToMob.scale(-1);
//        int density = GoalUtils.getCrowd(mob.level, target.position().add(targetToMob), CombatCircle.SHORT_DISTANCE).size();
//        int newdensity = GoalUtils.getCrowd(mob.level, target.position().add(alt), CombatCircle.SHORT_DISTANCE).size();
//        if (density > newdensity) targetToMob=alt;
        //consider two alternative paths at right angles
        for(Vec3 vec:ALTERNATIVES){
            Vec3 alt=targetToMob.cross(vec).normalize();
            int density = GoalUtils.getCrowd(mob.level, target.position().add(targetToMob), CombatCircle.CIRCLE_SIZE).size();
            int newdensity = GoalUtils.getCrowd(mob.level, target.position().add(targetToMob).add(alt), CombatCircle.CIRCLE_SIZE).size();
            if (density > newdensity) targetToMob=alt;
        }

        Vec3 relativeMoveTo=targetToMob.add(toAvoid).normalize().scale(safeDist);
        Vec3 moveTo = target.position().add(relativeMoveTo);
        this.path = this.pathNav.createPath(moveTo.x, moveTo.y, moveTo.z, 0);
        return this.path != null;
    }

    @Override
    public boolean canContinueToUse() {
        //becomes an attacker, does as they please
        boolean canAttack = mob.getTarget() != null && CombatManager.getManagerFor(mob.getTarget()).addAttacker(mob, null);
        return mob.getTarget()!=null&&!pathNav.isDone() && !canAttack;// && mob.level.getNearestEntity(Mob.class, SAME_TARGET, mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.SHORT_DISTANCE)).;// && !GoalUtils.socialDistancing(mob);
    }

    public void start() {
        mob.setAggressive(true);
    }

    public void stop() {
        restartPathingDelay=30;
    }

    public void tick() {
        restartPathingDelay--;
        final LivingEntity target = mob.getTarget();
        if (target == null) return;
        mob.lookAt(target, 30, 30);
        mob.getLookControl().setLookAt(target, 30, 30);
        double safeDist = Math.max(GeneralUtils.getAttributeValueSafe(target, ForgeMod.ATTACK_RANGE.get()) + 1, CombatCircle.SPREAD_DISTANCE);
        pathNav.moveTo(this.path, target.distanceToSqr(mob) > safeDist * safeDist ? walkSpeedModifier : sprintSpeedModifier);
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public EnumSet<Flag> getFlags() {
        return mutex;
    }
}
