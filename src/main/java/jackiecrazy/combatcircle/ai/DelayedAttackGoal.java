package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.utils.GeneralUtils;
import jackiecrazy.combatcircle.utils.MotionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.DamageSource;

import java.util.ArrayList;
import java.util.List;

public class DelayedAttackGoal extends Goal {
    private final DelayedAttack[] attackChain;
    private final MobEntity wielder;
    private final List<Entity> hitList = new ArrayList<>();
    private int ordinal;
    private int ticker;
    private int phase;

    public DelayedAttackGoal(MobEntity host, DelayedAttack... pattern) {
        wielder = host;
        attackChain = pattern;
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = wielder.getAttackTarget();
        DelayedAttack move = attackChain[ordinal];
        switch (move.getTaskType()) {
            case MELEEATTACK:
            case MELEEDEFENSE:
            case RANGEDATTACK:
            case RANGEDDEFENSE:
                return target != null && wielder.getDistanceSq(target) < move.getRadius() * move.getRadius();
            case RANDOM:
                return wielder.getRNG().nextInt(move.getMoveWeight()) == 0;
        }
        return true;
    }

    public boolean shouldContinueExecuting() {
        if (ordinal >= attackChain.length) return false;
        return !attackChain[ordinal].canEndEarly() | shouldExecute();
    }

    public boolean isPreemptible() {
        return false;
    }

    public void startExecuting() {
        ordinal = 0;
        resetTransients();
    }

    public void resetTask() {
        ordinal = 0;
        resetTransients();
    }

    public void tick() {
        DelayedAttack move = attackChain[ordinal];
        if (ticker == 0) {//begin move in moveset
            ticker++;
            wielder.setMotion(MotionUtils.convertToFacing(wielder, move.getStartVec()));
            return;
        }
        int timestamp = move.getFillTime();
        if (ticker < timestamp) {//winding up...
            ticker++;
            return;
        }
        if (phase < 1) {
            phase = 1;
            wielder.setMotion(MotionUtils.convertToFacing(wielder, move.getWindupVec()));
        }
        timestamp += move.getDamageTime();
        if (ticker < timestamp) {//deal damage
            ticker++;
            if (move.isIndiscriminate()) {
                for (Entity e : wielder.world.getEntitiesInAABBexcluding(wielder, wielder.getBoundingBox().grow(move.getRadius()), null)) {
                    if(GeneralUtils.isFacingEntity(wielder, e, move.getHorAngle(), move.getVertAngle())){
                        attack(e, move.getHealthDamage(), move.getHealthMultiplier());
                        hitList.add(e);
                    }
                }
            } else if (wielder.getAttackTarget() != null && wielder.getAttackTarget().getDistanceSq(wielder) < move.getRadius() * move.getRadius()) {
                wielder.attackEntityAsMob(wielder.getAttackTarget());
            }
            return;
        }
        if (phase < 2) {
            phase = 2;
            wielder.setMotion(MotionUtils.convertToFacing(wielder, move.getHitVec()));
        }
        timestamp += move.getEmptyTime();
        if (ticker < timestamp) {//cooldown
            ticker++;
            return;
        }
        //entire move ended
        wielder.setMotion(MotionUtils.convertToFacing(wielder, move.getEndVec()));
        ordinal++;
        resetTransients();
    }

    private void attack(Entity target, double basedmg, double multiplier){//TODO should this be a damage multiplier instead?
        AttributeModifier am=new AttributeModifier("temporary damage bonus", multiplier-1, AttributeModifier.Operation.MULTIPLY_BASE);
        double temp=wielder.getBaseAttributeValue(Attributes.ATTACK_DAMAGE);
        if(basedmg>0)
        wielder.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(basedmg);//FIXME does this update the value?
        wielder.getAttribute(Attributes.ATTACK_DAMAGE).applyNonPersistentModifier(am);
        wielder.attackEntityAsMob(target);
        wielder.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(temp);
        wielder.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(am);
    }

    private void resetTransients() {
        ticker = phase = 0;
        hitList.clear();
    }
}
