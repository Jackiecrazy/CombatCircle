package jackiecrazy.combatcircle.utils;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.OldMove;
import jackiecrazy.footwork.api.FootworkAttributes;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CombatManager {
    public static final ConcurrentHashMap<LivingEntity, CombatManager> managers = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Mob> mobList = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<Mob, Integer> attackList = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Mob> coolingList = new ConcurrentLinkedQueue<>();
    /*
                        The battle circle AI basically works like this (from an enemy's perspective):
First, walk towards the player until I get within a "danger" radius
While in "danger" mode, don't get too close to another enemy, unless I am given permission to attack the player.
Also while in "danger" mode, try to approach the player. If there are too many enemies in my way, I will effectively not be able to reach the player until the enemies move or the player moves.
When the player is in my "attack" radius (roughly the maximum range of my attack) ask the player if I'm allowed to attack. If so, add me to the list of current attackers on the player object.
If there are already the maximum allowed number of attackers on the list, I'm denied permission.
If I'm denied permission, try strafing for a second or two in a random direction until I'm given permission.
If the player moves out of attack range—even if I'm attacking—remove me from the attacker list.
If I die, or am stunned or otherwise unable to attack, remove me from the attacker list.
The maximum allowed number of simultaneous attackers is critical in balancing your battle circle. A higher number causes an exponential increase in pressure. In the example demo I have it set at 2; less twitchy and more "cinematic" games set it at 1. If you put this number too high, you defeat the purpose of the circle, because large groups of enemies become unassailable or can only be defeated with uninteresting poke-and-run tactics.
Of similar importance is the enemy attack rate. This is not the fastest possible attack rate of the enemy, but how often they will choose to attack when given permission.
As you would expect, a lower number increases pressure, but you should generally have this be several times higher than the real attack rate. You can make this rate a bit more unpredictable (and thus the amount of pressure slightly less predictable) by increasing attackRateFluctuation, which will increase or decrease the attack rate after each attack.
Mobs should move into a position that is close to the player, far from allies, and close to them.
                         */

    /*
    HO, YOU'RE APPROACHING ME?
    get within, say, 4 blocks
    send request to combat manager to enter list
    combat manager checks if there is space for mob, and if mob is not on cooling list
      if so, add to mobs that can approach the player, send slot data to mob
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

    private static LivingEntity rayTraceBetween(Entity one, Entity two) {
        Vec3 start = one.position();
        Vec3 end = two.position();
        Vec3 look = end.subtract(start);
        LivingEntity entity = null;
        List<LivingEntity> list = one.level.getEntitiesOfClass(LivingEntity.class, one.getBoundingBox().expandTowards(look.x, look.y, look.z).inflate(1.5), EntitySelector.LIVING_ENTITY_STILL_ALIVE);
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

    public boolean addAttacker(Mob m, OldMove move) {
        //what
        if (!addMob(m)) return false;
        //on cooldown list!
        if (isCooling(m)) return false;
        //dude you're already attacking
        if (hasAttacker(m)) return true;
        //no clear shot
        if (rayTraceBetween(m, target) != null)
            return false;
        //can we squeeze the move in
        float weight = getAttackWeight(m, move);
        if (alimit < currentAttack + weight) return false;
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
        return m.getBbWidth();
    }

    private float getAttackWeight(Mob m, OldMove move) {
        return 1;
    }

    public void tick() {
        purge.clear();
        //remove attackers that have attacked
        attackList.forEach((a, b) -> {
            if (!a.isAlive() || currentMob > 1 && (a.tickCount > b + CombatCircle.MAXIMUM_CHASE_TIME || a.getLastHurtByMobTimestamp() > b || a.getLastHurtMobTimestamp() > b)) {
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
        //remove cooling mobs
        if (purgeTimer > Math.min(currentMob * 5, 30)) {//scales on currentMob to prevent duels being weird
            if (!coolingList.isEmpty()) {
//                Mob m = coolingList.peek();
//                if (!m.isAlive() || m.distanceToSqr(target) > (CombatCircle.CIRCLE_SIZE * CombatCircle.CIRCLE_SIZE)) {
//                    coolingList.poll();
//                }
                coolingList.poll();
            }
            purgeTimer = 0;
        }
    }
}
