package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.utils.MotionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class DelayedAttackGoal extends Goal {
    private final OldMove[] attackChain;
    private final MobEntity wielder;
    private final List<Entity> hitList = new ArrayList<>();
    private final int cooldown;
    private int ordinal;
    private int ticker;
    private int phase;
    private int cooldownTicker;

    public DelayedAttackGoal(MobEntity host, int cooldown, OldMove... pattern) {
        wielder = host;
        this.cooldown = cooldown;
        attackChain = pattern;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = wielder.getTarget();
        if (cooldownTicker > 0) return false;
        if (ordinal >= attackChain.length) return false;
        OldMove move = attackChain[ordinal];
        switch (move.getTaskType()) {
            case MELEEATTACK:
            case MELEEDEFENSE:
            case RANGEDATTACK:
            case RANGEDDEFENSE:
                return target != null && wielder.distanceToSqr(target) < move.getInitiationRadius() * move.getInitiationRadius();
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
        if (ordinal >= attackChain.length) return;
        OldMove move = attackChain[ordinal];
        if (ticker == 0) {//begin move in moveset
            ticker++;
            wielder.setDeltaMovement(MotionUtils.convertToFacing(wielder, move.getStartVec()));
            return;
        }
        int timestamp = move.getFillTime();
        if (ticker < timestamp) {//winding up...
            ticker++;
            return;
        }
        if (phase < 1) {//windup-damage transition velocity
            phase = 1;
            wielder.setDeltaMovement(MotionUtils.convertToFacing(wielder, move.getWindupVec()));
            wielder.heal(move.getSelfHealing());
        }
        timestamp += move.getDamageTime();
        if (ticker < timestamp) {//deal damage
            ticker++;
            if (move.isIndiscriminate()) {
                for (Entity e : wielder.level.getEntities(wielder, wielder.getBoundingBox().inflate(move.getAttackRadius()), null)) {
                    if (!hitList.contains(e) && .isFacingEntity(wielder, e, move.getHorAngle() + move.getHorAngleOffset(), move.getVertAngle() + move.getVertAngleOffset())) {
                        attack(e, move.getHealthDamage(), move.getHealthMultiplier());
                        hitList.add(e);
                    }
                }
            } else if (wielder.getTarget() != null && !hitList.contains(wielder.getTarget()) && wielder.getTarget().distanceToSqr(wielder) < move.getAttackRadius() * move.getAttackRadius()) {
                attack(wielder.getTarget(), move.getHealthDamage(), move.getHealthMultiplier());
                hitList.add(wielder.getTarget());
            }
            return;
        }
        if (phase < 2) {//damage-cooldown transition velocity
            phase = 2;
            wielder.setDeltaMovement(MotionUtils.convertToFacing(wielder, move.getHitVec()));
        }
        timestamp += move.getEmptyTime();
        if (ticker < timestamp) {//cooldown
            ticker++;
            return;
        }
        //entire move ended
        wielder.setDeltaMovement(MotionUtils.convertToFacing(wielder, move.getEndVec()));
        ordinal++;
        resetTransients();
    }

    private void attack(Entity target, double basedmg, double multiplier) {//TODO should this be a damage multiplier instead?
        AttributeModifier am = new AttributeModifier("temporary damage bonus", multiplier - 1, AttributeModifier.Operation.MULTIPLY_BASE);
        double temp = wielder.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        if (basedmg > 0)
            wielder.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(basedmg);//FIXME does this update the value?
        wielder.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(am);
        wielder.doHurtTarget(target);
        wielder.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(temp);
        wielder.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(am);
    }

    private void resetTransients() {
        ticker = phase = 0;
        hitList.clear();
        cooldownTicker = cooldown;
    }

    @Override
    public EnumSet<Flag> getFlags() {
        EnumSet<Flag> ret = EnumSet.noneOf(Flag.class);
        if (ordinal >= attackChain.length) return ret;
        OldMove da = attackChain[ordinal];
        if (!da.canMove()) ret.add(Flag.MOVE);
        if (!da.canTurn()) ret.add(Flag.LOOK);
        return ret;
    }
}
