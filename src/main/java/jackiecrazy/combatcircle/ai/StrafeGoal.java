package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.combatcircle.utils.GoalUtils;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class StrafeGoal extends Goal {
    protected final PathNavigation pathNav;
    protected final int minDist;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    protected PathfinderMob mob;
    private final TargetingConditions SAME_TARGET = TargetingConditions.forNonCombat().selector(from -> {
        if (!(from instanceof Mob && mob != null)) return false;
        if (((Mob) from).getTarget() == null) return false;
        return ((Mob) from).getTarget() == mob.getTarget();
    });
    protected Path path;
    Vec3 toAvoid;
    private boolean retreated;

    public StrafeGoal(PathfinderMob entityIn) {
        super();
        mob = entityIn;
        walkSpeedModifier = 0.8f;
        sprintSpeedModifier = 1.2f;
        minDist = CombatCircle.CIRCLE_SIZE;
        pathNav = entityIn.getNavigation();
    }

    public boolean canUse() {
        final LivingEntity target = mob.getTarget();
        if (target == null) return false;
        if (cannotApproach(target)) return false;
        //if (GeneralUtils.getDistSqCompensated(target, mob) > (minDist + 2) * (minDist + 2)) return false;
        toAvoid = Vec3.ZERO;
        double safesq = Math.min(mob.distanceToSqr(target), mob.getBbWidth() * mob.getBbWidth() * 6);
        for (Entity fan : mob.level.getNearbyEntities(LivingEntity.class, SAME_TARGET, mob, mob.getBoundingBox().inflate(safesq))) {
            if (fan instanceof Monster mob2 && target == mob2.getTarget() &&
                    GeneralUtils.getDistSqCompensated(fan, mob) < (safesq) && fan != target) {
                //mobs "avoid" clumping together
                Vec3 diff = mob.position().subtract(fan.position());
                double targDistSq = diff.lengthSqr();
                //toAvoid = toAvoid.add(diff.normalize().scale(targDistSq));
            }
        }
        //slight sideways knockback
        Vec3 toTarget = target.position().subtract(mob.position()).normalize();
        toAvoid = toAvoid.normalize();
        toAvoid = toAvoid.subtract(toTarget.scale(toAvoid.dot(toTarget))).scale(mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
        Vec3 first = GeneralUtils.getPointInFrontOf(mob, target, -minDist);
        mob.knockback(toAvoid.x, 0, toAvoid.z);
        //
        Vec3 vector3d = DefaultRandomPos.getPosTowards(this.mob, minDist, minDist / 2, first.add(toAvoid), Math.PI / 2);//what is the last variable even
        if (vector3d == null) {
            return false;
        } else {
            int density = GoalUtils.getCrowd(mob.level, mob.blockPosition(), CombatCircle.SHORT_DISTANCE).size();
            int newdensity = GoalUtils.getCrowd(mob.level, vector3d, CombatCircle.SHORT_DISTANCE).size();
            if (density < newdensity) return false;
            this.path = this.pathNav.createPath(vector3d.x, vector3d.y, vector3d.z, 0);
            return this.path != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!retreated) return true;
        if (this.mob.getTarget() == null) {
            return false;
        }
        if (cannotApproach(mob.getTarget())) {
            return false;
        }
        BlockPos bp = mob.blockPosition();
        return !pathNav.isDone();// && mob.level.getNearestEntity(Mob.class, SAME_TARGET, mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.SHORT_DISTANCE)).;// && !GoalUtils.socialDistancing(mob);
    }

    public void start() {
        retreated = false;
    }

    public void stop() {
        retreated = false;
    }

    public void tick() {
        if (mob.getTarget() == null) return;
        mob.lookAt(mob.getTarget(), 30, 30);
        mob.getLookControl().setLookAt(mob.getTarget(), 30, 30);
        if (GeneralUtils.getDistSqCompensated(mob.getTarget(), mob) < 3.0) {
            pathNav.stop();
            mob.setDeltaMovement(mob.getDeltaMovement().add(mob.position().subtract(mob.getTarget().position()).normalize().scale(mob.getAttributeValue(Attributes.MOVEMENT_SPEED) / 3)));
            pathNav.setSpeedModifier(this.sprintSpeedModifier);
        } else {
            if (!this.retreated) {
                this.retreated = true;
                pathNav.moveTo(this.path, this.walkSpeedModifier);
            }
            pathNav.setSpeedModifier(this.sprintSpeedModifier);
        }

    }

    public boolean cannotApproach(LivingEntity target) {
        return CombatManager.getManagerFor(target).addAttacker(mob, null);
    }
}
