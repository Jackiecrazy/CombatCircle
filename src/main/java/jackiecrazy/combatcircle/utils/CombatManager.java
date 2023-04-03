package jackiecrazy.combatcircle.utils;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.OldMove;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CombatManager {
    public static final ConcurrentHashMap<LivingEntity, CombatManager> managers = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Mob> mobList = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<Mob, Integer> attackList = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Mob> coolingList = new ConcurrentLinkedQueue<>();
    /*
    HO, YOU'RE APPROACHING ME?
    get within, say, 4 blocks
    send request to combat manager to enter list
    combat manager checks if there is space for mob, and if mob is not on cooling list
      if so, add to mobs that can approach the player
      if not, tell mob continue outer strafing
    mobs within range will request for attack
    combat manager checks if attack limit has been reached
      if so, tell mob to inner strafe
      if not, allow attack

    each tick combat manager is pinged,
    > check if any mob is (taking more than 5 second to attack), beyond their move range, has been interrupted, or has finished
       if so, purge them from the attack list and order them to leave the circle, placing them on the cooling list
    > purge the first recorded mob from the cooling list if at least 10 ticks have elapsed
     */
    private final LivingEntity target;
    private final int mlimit, alimit;
    private final ArrayList<Mob> purge = new ArrayList<>();
    private float currentMob, currentAttack;
    private int purgeTimer;

    public CombatManager(LivingEntity p, int mobLimit, int attackLimit) {
        target = p;
        mlimit = mobLimit;
        alimit = attackLimit;
    }

    public static CombatManager getManagerFor(LivingEntity target) {
        return CombatManager.managers.computeIfAbsent(target, (a) -> new CombatManager(a, CombatCircle.MOB_LIMIT, CombatCircle.ATTACK_LIMIT));
    }

    public boolean addMob(Mob m) {
        if (isCooling(m)) return false;
        if (hasMob(m)) return true;
        float weight = getMobWeight(m);
        if (mlimit < currentMob + weight) return false;
        mobList.add(m);
        currentMob += weight;
        return true;
    }

    public boolean addAttacker(Mob m, OldMove move) {
        if (isCooling(m)) return false;
        //if (!hasMob(m)) return false;
        if (hasAttacker(m)) return true;
        float weight = getAttackWeight(m, move);
        if (alimit < currentAttack + weight) return false;
        if (!addMob(m)) return false;
        attackList.put(m, m.tickCount);
        currentAttack += weight;
        return true;
    }

    public void removeMob(Mob m) {
        if (!mobList.contains(m)) return;
        removeAttacker(m, null);
        mobList.remove(m);
        currentMob -= getMobWeight(m);
    }

    public void removeAttacker(Mob m, OldMove move) {
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
        return 1;//m.getBbWidth();
    }

    private float getAttackWeight(Mob m, OldMove move) {
        return 1;
    }

    public void tick() {
        purge.clear();
        attackList.forEach((a, b) -> {
            if (!a.isAlive() || a.tickCount > b + CombatCircle.MAXIMUM_CHASE_TIME || a.getLastHurtByMobTimestamp() > b || a.getLastHurtMobTimestamp() > b) {
                purge.add(a);
                purgeTimer = 0;
            }
        });
        mobList.forEach(a -> {
            if (!a.isAlive()) {
                purge.add(a);
            }
        });
        purge.forEach(this::removeMob);
        purgeTimer++;
        if (purgeTimer > 30) {
            if (!coolingList.isEmpty()) {
                Mob m = coolingList.peek();
                if (!m.isAlive() || m.distanceToSqr(target) > (CombatCircle.CIRCLE_SIZE * CombatCircle.CIRCLE_SIZE)) {
                    coolingList.poll();
                }
            }
            purgeTimer = 0;
        }
    }
}
