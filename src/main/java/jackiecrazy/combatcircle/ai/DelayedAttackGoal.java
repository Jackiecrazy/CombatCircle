package jackiecrazy.combatcircle.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;

import java.util.ArrayList;
import java.util.List;

public class DelayedAttackGoal extends Goal {
    private final DelayedAttack[] attackChain;
    private final MobEntity wielder;
    private final List<LivingEntity> hitList = new ArrayList<>();
    private int ordinal;
    private int ticker;
    private int phase;

    public DelayedAttackGoal(MobEntity host, DelayedAttack... pattern) {
        wielder = host;
        attackChain = pattern;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = wielder.getTarget();
        DelayedAttack move = attackChain[ordinal];
        switch (move.getTaskType()) {
            case MELEEATTACK:
            case MELEEDEFENSE:
            case RANGEDATTACK:
            case RANGEDDEFENSE:
                return target != null && wielder.distanceToSqr(target) < move.getRadius() * move.getRadius();
            case RANDOM:
                return wielder.getRandom().nextInt(move.getMoveWeight()) == 0;
        }
        return true;
    }

    public boolean canContinueToUse() {
        if (ordinal >= attackChain.length) return false;
        return !attackChain[ordinal].canEndEarly() | canUse();
    }

    public boolean isInterruptable() {
        return false;
    }

    public void start() {
        ordinal = 0;
        resetTransients();
    }

    public void stop() {
        ordinal = 0;
        resetTransients();
    }

    public void tick() {
        DelayedAttack move = attackChain[ordinal];
        if (ticker == 0) {//begin move in moveset
            ticker++;
            return;//TODO initial velocity
        }
        int timestamp = move.getFillTime();
        if (ticker < timestamp) {//winding up...
            ticker++;
            return;
        }
        if (phase < 1) {
            phase = 1;
            //TODO end of windup velocity
        }
        timestamp += move.getDamageTime();
        if (ticker < timestamp) {//deal damage
            ticker++;
            if (move.isIndiscriminate()) {
                for (Entity e : wielder.level.getEntities(wielder, wielder.getBoundingBox().inflate(move.getRadius()), null)) {

                }
            } else if (wielder.getTarget() != null && wielder.getTarget().distanceToSqr(wielder) < move.getRadius() * move.getRadius()) {
                wielder.doHurtTarget(wielder.getTarget());
            }
            return;
        }
        if (phase < 2) {
            phase = 2;
            //TODO end of damage velocity
        }
        timestamp += move.getEmptyTime();
        if (ticker < timestamp) {//cooldown
            ticker++;
            return;
        }
        //entire move ended
        //TODO final velocity
        ordinal++;
        resetTransients();
    }

    private void attack(Entity target, double basedmg, boolean fixed){//TODO should this be a damage multiplier instead?
        double temp=wielder.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        wielder.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(basedmg);//FIXME does this update the value?
        wielder.doHurtTarget(target);
        wielder.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(temp);
    }

    private void resetTransients() {
        ticker = phase = 0;
        hitList.clear();
    }
}
