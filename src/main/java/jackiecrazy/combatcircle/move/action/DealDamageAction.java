package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.DamageArgument;
import jackiecrazy.footwork.api.CombatDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DealDamageAction extends Action {

    private Argument<Double> amount;
    private DamageArgument damage_source;

    private List<Action> on_hit = new ArrayList<>();
    private List<Action> on_damage = new ArrayList<>();
    private List<Action> on_kill = new ArrayList<>();

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        DamageSource baked = damage_source.resolve(wrapper, parent, performer, target);
        boolean success = target.hurt(baked, amount.resolve(wrapper, parent, performer, target).floatValue());
        if (baked instanceof CombatDamageSource cds)
            performer.getPersistentData().putFloat("combatcircle:finalized_damage", cds.getFinalDamage());
        int ret = runActions(wrapper, parent, on_hit, performer, target);
        if (success) {
            if (!target.isAlive()) {
                int damageRet = runActions(wrapper, parent, on_kill, performer, target);
                if (damageRet != 0) ret = damageRet;
            }
            int damageRet = runActions(wrapper, parent, on_damage, performer, target);
            if (damageRet != 0) ret = damageRet;
        }
        return ret;
    }
}
