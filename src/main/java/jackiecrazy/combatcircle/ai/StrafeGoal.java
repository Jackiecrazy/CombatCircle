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
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class StrafeGoal extends Goal {
    private static final EntityPredicate SAME_TARGET = new EntityPredicate() {
        @ParametersAreNonnullByDefault
        public boolean test(@Nullable LivingEntity from, LivingEntity to) {
            if (!(from instanceof MobEntity && to instanceof MobEntity)) return false;
            if (((MobEntity) from).getTarget() == null) return false;
            return ((MobEntity) from).getTarget() == ((MobEntity) to).getTarget();
        }
    };
    protected final CreatureEntity mob;
    protected final PathNavigator pathNav;
    protected final int minDist;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    protected LivingEntity toAvoid;
    protected Path path;

    public StrafeGoal(CreatureEntity entityIn) {
        super();
        mob = entityIn;
        walkSpeedModifier = 0.8f;
        sprintSpeedModifier = 1.2f;
        minDist = CombatCircle.CIRCLE_SIZE;
        pathNav = entityIn.getNavigation();
    }

    public boolean canUse() {
        if (mob.getTarget() == null) return false;
        if (cannotApproach(mob.getTarget())) return false;
        if (GeneralUtils.getDistSqCompensated(mob.getTarget(), mob) > (minDist + 2) * (minDist + 2)) return false;
        BlockPos bp = mob.blockPosition();
        toAvoid = mob.level.getNearestEntity(MobEntity.class, SAME_TARGET, mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.SHORT_DISTANCE));
        if (this.toAvoid == null) {
            return false;
        } else {
            Vector3d first = GeneralUtils.getPointInFrontOf(mob, toAvoid, -minDist);
            Vector3d second = GeneralUtils.getPointInFrontOf(mob, mob.getTarget(), -minDist).subtract(mob.getTarget().position()).normalize();
            Vector3d vector3d = RandomPositionGenerator.getPosTowards(this.mob, minDist, minDist / 2, first.add(second));
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
    }

    @Override
    public boolean canContinueToUse() {
        BlockPos bp = mob.blockPosition();
        return !pathNav.isDone() && toAvoid == mob.level.getNearestEntity(MobEntity.class, SAME_TARGET, mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.SHORT_DISTANCE));// && !GoalUtils.socialDistancing(mob);
    }

    public boolean cannotApproach(LivingEntity target) {
        return CombatManager.getManagerFor(target).addAttacker(mob, null);
    }

    public void start() {
        this.pathNav.moveTo(this.path, this.walkSpeedModifier);
    }

    public void stop() {
        this.toAvoid = null;
    }

    public void tick() {
        if (GeneralUtils.getDistSqCompensated(toAvoid, mob) < minDist * minDist) {
            this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
        } else {
            this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
        }

    }
}
