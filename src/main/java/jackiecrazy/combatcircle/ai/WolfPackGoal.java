package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.Movesets;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.footwork.api.FootworkAttributes;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.awt.*;
import java.util.EnumSet;
import java.util.Random;

/**
 * this goal causes mobs to factor in other mobs and try to move away from them when near the player, unless they are attacking
 */
public class WolfPackGoal extends Goal {
    static final EnumSet<Flag> mutex = EnumSet.allOf(Flag.class);
    private static final Random r = new Random();
    private static final Vec3[] ALTERNATIVES = {new Vec3(0, 1, 0), new Vec3(0, -1, 0)};
    protected final PathNavigation pathNav;
    protected final double slightSpread;
    private final Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    protected LivingEntity targetCopy;//pillagers are stupid
    protected PathfinderMob mob;
    private final TargetingConditions SAME_TARGET = TargetingConditions.forNonCombat().selector(from -> {
        if (!(from instanceof Mob && mob != null)) return false;
        if (((Mob) from).getTarget() == null) return false;
        return ((Mob) from).getTarget() == mob.getTarget();
    });
    protected Path path;
    float strafeX, strafeZ;
    private int retryCooldown = 0;

    public WolfPackGoal(PathfinderMob entityIn) {
        super();
        mob = entityIn;
        walkSpeedModifier = Movesets.moves.getOrDefault(entityIn.getType(), Movesets.DEFAULT).pursue_walk_speed;
        sprintSpeedModifier =  Movesets.moves.getOrDefault(entityIn.getType(), Movesets.DEFAULT).pursue_sprint_speed;
        slightSpread = Math.random() * 2 - 1;
        pathNav = entityIn.getNavigation();
    }

    public static Vec3 rotateVectorCC(Vec3 vec, Vec3 axis, double theta) {
        double x, y, z;
        double u, v, w;
        x = vec.x();
        y = vec.y();
        z = vec.z();
        u = axis.x();
        v = axis.y();
        w = axis.z();
        final double v1 = u * x + v * y + w * z;
        double xPrime = u * v1 * (1d - Math.cos(theta))
                + x * Math.cos(theta)
                + (-w * y + v * z) * Math.sin(theta);
        double yPrime = v * v1 * (1d - Math.cos(theta))
                + y * Math.cos(theta)
                + (w * x - u * z) * Math.sin(theta);
        double zPrime = w * v1 * (1d - Math.cos(theta))
                + z * Math.cos(theta)
                + (-v * x + u * y) * Math.sin(theta);
        return new Vec3(xPrime, yPrime, zPrime);
    }

    public boolean canUse() {
        //enforced pathing failure cooldown to help with bridges?
        if (retryCooldown-- > 0) return false;
        final LivingEntity target = mob.getTarget();
        //only applies to players
        if (target == null) return false;
        //there is no circle
        if (CombatManager.getManagerFor(target).getAllAttackers().size() < 2) return false;
        //if (!(target instanceof Player)) return false;
        //determine safe distance from player
        double safeDist = Math.max(GeneralUtils.getAttributeValueSafe(target, ForgeMod.ENTITY_REACH.get()), GeneralUtils.getAttributeValueSafe(mob, FootworkAttributes.ENCIRCLEMENT_DISTANCE.get())) + slightSpread;
        //in a decent sweet zone, so whatever
        double currentDist = target.distanceToSqr(mob);
        double safeDistSq = safeDist * safeDist;
        if (currentDist < safeDistSq && currentDist > safeDistSq * 0.8) return false;
        //becomes an attacker, moves as they please, differential handling for smart and dumb mobs
        if (Movesets.moves.containsKey(mob.getType())) {
            if (CombatManager.getManagerFor(target).hasAttacker(mob))
                return false;
        } else if (CombatManager.getManagerFor(target).addAttacker(mob, null)) return false;

        //start with random base vector to emulate natural strafe
        Vec3 toAvoid = Vec3.ZERO;//new Vec3(Math.random(), Math.random(), Math.random());
        Vec3 look = Vec3.ZERO;
        //try to maintain distance from others in a close-ish radius
        //for (Mob mob2 : mob.level().getNearbyEntities(Mob.class, SAME_TARGET, mob, mob.getBoundingBox().inflate(5))) {
        final Vec3 targetToMob = mob.position().subtract(target.position());
        for (Mob mob2 : CombatManager.getManagerFor(target).getAllAttackers()) {
            //determine if there are same target mobs too close and in safe range
            if (mob2.distanceToSqr(target) > safeDistSq && !mob2.isPassengerOfSameVehicle(mob)) {//target == mob2.getTarget() &&
                //add to avoid vector
                //prefer moving away from the player slightly to prevent jostling each other

                //how can we make sure that mobs don't end up moving into each other with attraction vector?
                final Vec3 secondToFirst = mob.position().subtract(mob2.position());
                float sideWeighting = mob2.getBbWidth();
                //add a significant side vector if mob is attacking to move out of the way of shots
                if (CombatManager.getManagerFor(target).hasAttacker(mob2)) sideWeighting *= 3;
                Vec3 diff = secondToFirst.add(targetToMob.normalize().scale(0.5)).scale(sideWeighting);
                toAvoid = toAvoid.add(diff);//.add(diff.normalize().scale(other.getBbWidth()/Math.min(1, diff.lengthSqr())));
                //flock
                look.add(mob2.getLookAngle());
            }
        }
        //normalize to reduce vector influence, more influence is exerted closer to the player
        //toAvoid = toAvoid.normalize().scale(num);
        //move to
        Vec3 moveTo = target.position().add(targetToMob.add(look.normalize()).add(toAvoid.normalize()).normalize().scale(safeDist));
        boolean prev = path == null || path.isDone();
        this.path = this.pathNav.createPath(moveTo.x, moveTo.y, moveTo.z, 0);
        if (path != null && prev) {
            //particle the path
            Node n = path.getEndNode();
            //ParticleUtils.playSweepParticle(FootworkParticles.LINE.get(), mob, new Vec3(n.x, n.y, n.z), 0, 1, c, 2);
        }
        targetCopy = target;
        return this.path != null;
    }

    @Override
    public boolean canContinueToUse() {
        //becomes an attacker, does as they please
        boolean canAttack = targetCopy != null && CombatManager.getManagerFor(targetCopy).hasAttacker(mob);
//        if (canAttack)
//            ParticleUtils.playSweepParticle(FootworkParticles.CIRCLE.get(), mob, mob.position(), 0, 1, c, 2);
//        if (pathNav.isDone())
//            ParticleUtils.playSweepParticle(FootworkParticles.BONK.get(), mob, mob.position(), 0, 1, c, 2);
        return targetCopy != null && !pathNav.isDone() && !canAttack;// && mob.level.getNearestEntity(Mob.class, SAME_TARGET, mob, bp.getX(), bp.getY(), bp.getZ(), mob.getBoundingBox().inflate(CombatCircle.SHORT_DISTANCE)).;// && !GoalUtils.socialDistancing(mob);
    }

    @Override
    public boolean isInterruptable() {
        return true;
    }

    public void start() {
        if (targetCopy == null) return;
        double safeDist = Math.max(GeneralUtils.getAttributeValueSafe(targetCopy, ForgeMod.ENTITY_REACH.get()) + 1, CombatCircle.SPREAD_DISTANCE);
        //we cannot control horizontal movement purely via strafe because drowned don't strafe
        //MOJAAAAAAAAAAAAANG
        //TODO can we also use wolf pack to have ranged units strafe out from behind other mobs to make their shot before hiding again?
        // I don't see a good way to move the mob itself, but we can move the mobs around the ranged attacker to clear a path.
        // Another way would be to project an indiscriminate "virtual" hitbox to tell others to move out of the way
        pathNav.moveTo(this.path, targetCopy.distanceToSqr(mob) > safeDist * safeDist ? walkSpeedModifier : sprintSpeedModifier);
        strafeX = CombatCircle.rand.nextFloat() * 0.5f - 0.25f;
        strafeZ = CombatCircle.rand.nextFloat() * 0.15f;
    }

    public void stop() {
        //ParticleUtils.playSweepParticle(FootworkParticles.IMPACT.get(), mob, mob.position(), 0, 1, c, 2);
        this.mob.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        //can't do anything about facing, what a pain.
//
        if (targetCopy == null) return;
        mob.lookAt(targetCopy, 30, 30);
        mob.getLookControl().setLookAt(targetCopy, 30, 30);
    }

    @Override
    public EnumSet<Flag> getFlags() {
        return mutex;
    }
}
