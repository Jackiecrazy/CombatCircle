package jackiecrazy.combatcircle.ai;

import com.mojang.math.Vector3d;
import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.combatcircle.utils.GoalUtils;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class StrafeGoal extends Goal {
    private static final TargetingConditions SAME_TARGET = new TargetingConditions() {
        @ParametersAreNonnullByDefault
        public boolean test(@Nullable LivingEntity from, LivingEntity to) {
            if (!(from instanceof Mob && to instanceof Mob)) return false;
            if (((Mob) from).getTarget() == null) return false;
            return ((Mob) from).getTarget() == ((Mob) to).getTarget();
        }
    };
    protected final PathfinderMob mob;
    protected final PathNavigation pathNav;
    protected final int minDist;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    protected Path path;
    Mob toAvoid;
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
        if (mob.getTarget() == null) return false;
        if (cannotApproach(mob.getTarget())) return false;
        if (GeneralUtils.getDistSqCompensated(mob.getTarget(), mob) > (minDist + 2) * (minDist + 2)) return false;
        BlockPos bp = mob.blockPosition();
        toAvoid = mob.level.getNearestEntity(Mob.class, SAME_TARGET.selector(), mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.SHORT_DISTANCE));
        Vec3 first = GeneralUtils.getPointInFrontOf(mob, mob.getTarget(), -minDist);
        Vec3 second = Vec3.ZERO;
        if (toAvoid != null)
            second = GeneralUtils.getPointInFrontOf(mob, toAvoid, -minDist).subtract(mob.getTarget().position()).normalize();
        Vec3 vector3d = RandomPos.generateRandomPosTowardDirection(this.mob, minDist, minDist / 2, first.add(second));
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
        if (CombatManager.getManagerFor(this.mob.getTarget()).hasAttacker(mob)) {
            return false;
        }
        BlockPos bp = mob.blockPosition();
        return !pathNav.isDone() && toAvoid == mob.level.getNearestEntity(Mob.class, SAME_TARGET, mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.SHORT_DISTANCE));// && !GoalUtils.socialDistancing(mob);
    }

    public boolean cannotApproach(LivingEntity target) {
        return CombatManager.getManagerFor(target).addAttacker(mob, null);
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
            mob.setDeltaMovement(mob.getDeltaMovement().add(mob.position().subtract(mob.getTarget().position()).normalize().scale(0.1)));
            pathNav.setSpeedModifier(this.sprintSpeedModifier);
        } else {
            if (!this.retreated) {
                this.retreated = true;
                pathNav.moveTo(this.path, this.walkSpeedModifier);
            }
            pathNav.setSpeedModifier(this.sprintSpeedModifier);
        }

    }
}
