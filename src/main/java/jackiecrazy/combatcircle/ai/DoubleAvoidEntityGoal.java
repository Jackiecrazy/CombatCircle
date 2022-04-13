package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.combatcircle.utils.GoalUtils;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class DoubleAvoidEntityGoal extends AvoidEntityGoal<LivingEntity> {
    private static final EntityPredicate SAME_TARGET = new EntityPredicate() {
        @ParametersAreNonnullByDefault
        public boolean test(@Nullable LivingEntity from, LivingEntity to) {
            if (!(from instanceof MobEntity && to instanceof MobEntity)) return false;
            if (((MobEntity) from).getTarget() == null) return false;
            return ((MobEntity) from).getTarget() == ((MobEntity) to).getTarget();
        }
    };
    protected final int minDist;

    public DoubleAvoidEntityGoal(CreatureEntity entityIn, int distFromTarget) {
        super(entityIn, LivingEntity.class, distFromTarget, 0.8, 1.2);
        minDist = distFromTarget;
    }

    public boolean canUse() {
        if (mob.getTarget() == null) return false;
        if (GeneralUtils.getDistSqCompensated(mob.getTarget(), mob) > (minDist + 2) * (minDist + 2)) return false;
        if (!cannotApproach(mob.getTarget())) return false;
        BlockPos bp = mob.blockPosition();
        toAvoid = mob.level.getNearestEntity(MobEntity.class, SAME_TARGET, mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.INNER_DISTANCE));
        if (this.toAvoid == null) {
            return false;
        } else {
            Vector3d first = GeneralUtils.getPointInFrontOf(mob, toAvoid, -maxDist);
            Vector3d second = GeneralUtils.getPointInFrontOf(mob, mob.getTarget(), -maxDist).subtract(mob.getTarget().position()).normalize();
            Vector3d vector3d = RandomPositionGenerator.getPosTowards(this.mob, minDist, minDist/2, first.add(second));
            if (vector3d == null) {
                return false;
            } else {
                int density = GoalUtils.getCrowd(mob.level, mob.blockPosition(), CombatCircle.INNER_DISTANCE).size();
                int newdensity = GoalUtils.getCrowd(mob.level, vector3d, CombatCircle.INNER_DISTANCE).size();
                if (density < newdensity) return false;
                this.path = this.pathNav.createPath(vector3d.x, vector3d.y, vector3d.z, 0);
                return this.path != null;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        BlockPos bp = mob.blockPosition();
        return super.canContinueToUse() && toAvoid == mob.level.getNearestEntity(MobEntity.class, SAME_TARGET, mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.INNER_DISTANCE));// && !GoalUtils.socialDistancing(mob);
    }

    public abstract boolean cannotApproach(LivingEntity target);

    public static class OuterStrafeGoal extends DoubleAvoidEntityGoal {

        public OuterStrafeGoal(CreatureEntity entityIn) {
            super(entityIn, CombatCircle.OUTER_DISTANCE);
        }

        @Override
        public boolean cannotApproach(LivingEntity target) {
            return !CombatManager.getManagerFor(target).addMob(mob);
        }
    }

    public static class InnerStrafeGoal extends DoubleAvoidEntityGoal {

        public InnerStrafeGoal(CreatureEntity entityIn) {
            super(entityIn, CombatCircle.INNER_DISTANCE);
        }

        @Override
        public boolean cannotApproach(LivingEntity target) {
            return !CombatManager.getManagerFor(target).addAttacker(mob, null);
        }
    }
}
