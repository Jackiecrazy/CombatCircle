package jackiecrazy.combatcircle.utils;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.ai.MovesetGoal;
import jackiecrazy.combatcircle.move.Movesets;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CombatManager {
    public static final ConcurrentHashMap<LivingEntity, CombatManager> managers = new ConcurrentHashMap<>();
    private static final CombatManager dummy = new CombatManager(null, 0, 0);
    private final ConcurrentLinkedQueue<Mob> mobList = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Node> nodeCache = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<Mob, Integer> attackList = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Mob> coolingList = new ConcurrentLinkedQueue<>();
    private final LivingEntity target;

    /*
    HO, YOU'RE APPROACHING ME?
    get within, say, 6 blocks
    send request to combat manager to enter attacker list with moveset
    combat manager checks if there is space for mob, if attack limit has been reached, and if mob is not on cooling list
      if so, add to mobs that can approach the player, send slot data to mob
      if not, tell mob continue outer strafing
    mobs within range will attack when their conditions have been met
      after attack finish, dead, or took too long, remove from attacker set, mob returns to outer circle after removal

    each tick combat manager is pinged,
    > check if any mob is (taking more than 5 second to attack), beyond their move range, has been interrupted, or has finished
       if so, purge them from the attack list and order them to leave the circle, placing them on the cooling list
    > purge the first recorded mob from the cooling list if at least 10 ticks have elapsed

    mlimit for mobs that form the inner circle, alimit for attacks
     */
    private final int mlimit, alimit;
    private final ArrayList<Mob> purge = new ArrayList<>();
    private float currentMob, currentAttack;
    private int purgeTimer;

    public CombatManager(LivingEntity p, int mobLimit, int attackLimit) {
        target = p;
        mlimit = mobLimit;
        alimit = attackLimit;
    }

    @NotNull
    public static CombatManager getManagerFor(@Nullable LivingEntity target) {
        if (target == null) return dummy;
        return CombatManager.managers.computeIfAbsent(target, (a) -> new CombatManager(a, CombatCircle.MOB_LIMIT, CombatCircle.ATTACK_LIMIT));
    }

    private static LivingEntity rayTraceBetween(Entity one, Entity two) {
        Vec3 start = one.position();
        Vec3 end = two.position();
        Vec3 look = end.subtract(start);
        LivingEntity entity = null;
        List<LivingEntity> list = one.level().getEntitiesOfClass(LivingEntity.class, one.getBoundingBox().expandTowards(look.x, look.y, look.z).inflate(1.5), EntitySelector.LIVING_ENTITY_STILL_ALIVE);
        double d0 = -1.0D;//necessary to prevent small derps

        for (LivingEntity entity1 : list) {
            if (entity1 != one && entity1 != two && entity1 instanceof Monster) {
                AABB axisalignedbb = entity1.getBoundingBox();
                Optional<Vec3> raytraceresult = axisalignedbb.clip(start, end);
                if (raytraceresult.isPresent()) {
                    return entity1;
                }
            }
        }
        return entity;
    }

    public ConcurrentLinkedQueue<Node> getNodeCache() {
        return nodeCache;
    }

    public ConcurrentLinkedQueue<Mob> getAllAttackers() {
        return mobList;
    }

    public boolean addMob(Mob m) {
        if (hasMob(m)) return true;
        float weight = getMobWeight(m);
        mobList.add(m);
        currentMob += weight;
        return true;
    }

    public boolean addAttacker(Mob m, MovesetGoal move) {
        //what
        if (!addMob(m)) return false;
        //on cooldown list!
        if (isCooling(m)) return false;
        //dude you're already attacking
        if (hasAttacker(m)) return true;
        //no clear shot
//        if (rayTraceBetween(m, target) != null)
//            return false;
        //can we squeeze the move in
        float weight = getAttackWeight(m, move);
        if (alimit < currentAttack + weight) return false;
        attackList.put(m, m.tickCount);
        currentAttack += weight;
        return true;
    }

    public float getRemainingAttackPower() {
        return alimit - currentAttack;
    }

    public void removeMob(Mob m) {
        if (!mobList.contains(m)) return;
        removeAttacker(m, null);
        mobList.remove(m);
        currentMob -= getMobWeight(m);
    }

    public void removeAttacker(Mob m, MovesetGoal move) {
        if (!attackList.containsKey(m)) return;
        attackList.remove(m);
        currentAttack -= getAttackWeight(m, move);
        coolingList.add(m);
    }

    public boolean hasMob(Mob m) {
        return mobList.contains(m);
    }

    public boolean hasAttacker(Mob m) {
        return attackList.containsKey(m);
    }

    public boolean isCooling(Mob m) {
        return coolingList.contains(m);
    }

    private float getMobWeight(Mob m) {
        return m.getBbWidth();
    }

    private float getAttackWeight(Mob m, MovesetGoal move) {
        if (move != null) return move.getWeight();
        return 1;
    }

    public void tick() {
        purge.clear();
        //remove dumb attackers that have attacked
        attackList.forEach((a, b) -> {
            if (!a.isAlive() || currentMob > 1 && !Movesets.moves.containsKey(a.getType()) && (a.tickCount > b + CombatCircle.MAXIMUM_CHASE_TIME || a.getLastHurtMobTimestamp() > b)) {
                purge.add(a);
                purgeTimer = 0;
            }
        });
        purge.forEach(a -> this.removeAttacker(a, null));
        purge.clear();
        //remove dead mobs to prevent memory leak
        mobList.forEach(a -> {
            if (!a.isAlive()) {
                purge.add(a);
            }
        });
        purge.forEach(this::removeMob);
        purgeTimer++;
        purge.clear();
        //remove cooling mobs
        if (purgeTimer > Math.min(currentMob * 7, 20)) {//scales on currentMob to prevent duels being weird
            if (!coolingList.isEmpty()) {
//                Mob m = coolingList.peek();
//                if (!m.isAlive() || m.distanceToSqr(target) > (CombatCircle.CIRCLE_SIZE * CombatCircle.CIRCLE_SIZE)) {
//                    coolingList.poll();
//                }
                coolingList.poll();
            }
            purgeTimer = 0;
        }
        //keep a set of nodes
//        nodeCache.clear();
//        nodeCache.addAll(mobList.stream().map(a -> {
//            Node ret = new Node(a.getBlockX(), a.getBlockY(), a.getBlockZ());
//            ret.f = a.getBbWidth();
//            ret.g = a.getBbHeight();
//            return ret;
//        }).collect(Collectors.toSet()));
    }
}
