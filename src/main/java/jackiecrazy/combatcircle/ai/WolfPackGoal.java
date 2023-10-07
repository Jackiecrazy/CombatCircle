package jackiecrazy.combatcircle.ai;

import com.mojang.math.Vector3f;
import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.combatcircle.utils.GoalUtils;
import jackiecrazy.footwork.api.FootworkAttributes;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.EnumSet;

/**
 * this goal causes mobs to factor in other mobs and try to move away from them when near the player, unless they are attacking
 */
public class WolfPackGoal extends Goal {

    static final EnumSet<Flag> mutex = EnumSet.allOf(Flag.class);
    private static final Vec3[] ALTERNATIVES = {new Vec3(0, 1, 0), new Vec3(0, -1, 0)};
    protected final PathNavigation pathNav;
    protected final double slightSpread;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    protected PathfinderMob mob;
    private final TargetingConditions SAME_TARGET = TargetingConditions.forNonCombat().selector(from -> {
        if (!(from instanceof Mob && mob != null)) return false;
        if (((Mob) from).getTarget() == null) return false;
        return ((Mob) from).getTarget() == mob.getTarget();
    });
    protected Path path;
    private int retryCooldown = 0;

    public WolfPackGoal(PathfinderMob entityIn) {
        super();
        mob = entityIn;
        walkSpeedModifier = 0.8f;
        sprintSpeedModifier = 1.2f;
        slightSpread = Math.random();
        pathNav = entityIn.getNavigation();
    }

    public boolean canUse() {
        //enforced pathing failure cooldown to help with bridges?
        if (retryCooldown-- > 0) return false;
        final LivingEntity target = mob.getTarget();
        if (target == null) return false;
        //too far away, just charge in
        //if (target.distanceToSqr(mob) > CombatCircle.SPREAD_DISTANCE * CombatCircle.SPREAD_DISTANCE * 4) return false;
        //becomes an attacker, moves as they please
        if (CombatManager.getManagerFor(target).addAttacker(mob, null)) return false;
        //there is no circle
        if (CombatManager.getManagerFor(target).getAllAttackers().size() < 1) return false;

        //determine safe distance from player
        double safeDist = Math.max(GeneralUtils.getAttributeValueSafe(target, ForgeMod.ATTACK_RANGE.get()), GeneralUtils.getAttributeValueSafe(mob, FootworkAttributes.ENCIRCLEMENT_DISTANCE.get())) + slightSpread;
        //start with random base vector to emulate natural strafe
        Vec3 toAvoid = Vec3.ZERO;//new Vec3(Math.random(), Math.random(), Math.random());
        int num = 0;
        //try to maintain distance from others in a close-ish radius
        for (Mob mob2 : CombatManager.getManagerFor(target).getAllAttackers()) {
            //determine if there are same target mobs too close and in safe range
            if (mob2.distanceToSqr(target) > safeDist * safeDist && mob2.distanceToSqr(mob) < safeDist * safeDist && !mob2.isPassengerOfSameVehicle(mob)) {//target == mob2.getTarget() &&
                //add to avoid vector
                Vec3 diff = mob.position().subtract(mob2.position());
                toAvoid = toAvoid.add(diff);//.add(diff.normalize().scale(other.getBbWidth()/Math.min(1, diff.lengthSqr())));
                num++;
            }
        }
        //normalize to reduce vector influence, more influence is exerted closer to the player
        //toAvoid = toAvoid.normalize().scale(num);
        //move to
        Vec3 moveTo = target.position().add(mob.position().subtract(target.position()).add(toAvoid.normalize()).normalize().scale(safeDist));
        this.path = this.pathNav.createPath(moveTo.x, moveTo.y, moveTo.z, 0);
        return this.path != null;
    }

    @Override
    public boolean canContinueToUse() {
        //becomes an attacker, does as they please
        boolean canAttack = mob.getTarget() != null && CombatManager.getManagerFor(mob.getTarget()).addAttacker(mob, null);
        return mob.getTarget() != null && !pathNav.isDone() && !canAttack;// && mob.level.getNearestEntity(Mob.class, SAME_TARGET, mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.SHORT_DISTANCE)).;// && !GoalUtils.socialDistancing(mob);
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    public void start() {
        mob.setAggressive(true);
        final LivingEntity target = mob.getTarget();
        if (target == null) return;
        double safeDist = Math.max(GeneralUtils.getAttributeValueSafe(target, ForgeMod.ATTACK_RANGE.get()) + 1, CombatCircle.SPREAD_DISTANCE);
        pathNav.moveTo(this.path, target.distanceToSqr(mob) > safeDist * safeDist ? walkSpeedModifier : sprintSpeedModifier);
    }

    public void stop() {
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        final LivingEntity target = mob.getTarget();
        if (target == null) return;
        mob.lookAt(target, 30, 30);
        mob.getLookControl().setLookAt(target, 30, 30);
    }

    @Override
    public EnumSet<Flag> getFlags() {
        return mutex;
    }
}
