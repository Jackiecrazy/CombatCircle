package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.combatcircle.utils.GoalUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * this goal causes mobs to spread out to predefined slots around the player, unless they are attacking
 * the goal does not know about which empty slots exist. This should be provided by the combat manager.
 * todo how will we model opportunism? Mobs get their slots when they ask for them, not when they are necessarily the closest.
 * "closest" would only be known by the manager
 */
public class CinematicCircleGoal extends Goal {
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

    public CinematicCircleGoal(PathfinderMob entityIn) {
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
        if(CombatManager.getManagerFor(target).addAttacker(mob, null)) return false;
        Vec3 vector3d;
        final List<Mob> attackers = CombatManager.getManagerFor(target).getAllAttackers().stream().toList();
        int size = attackers.size();
        double radius = 5;
        double t = ((attackers.indexOf(mob)) * (Math.PI * 2) / (size));
        //find next spot in circle
        vector3d = target.position().add(radius * Math.cos(t), 0, radius * Math.sin(t));
        //move there gogogo
        this.path = this.pathNav.createPath(vector3d.x, vector3d.y, vector3d.z, 0);
        return this.path != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.getTarget() == null) return false;
        if(CombatManager.getManagerFor(mob.getTarget()).addAttacker(mob, null)) return false;
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
        pathNav.moveTo(this.path, this.walkSpeedModifier);
    }
}
