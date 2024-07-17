package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.DamageArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.footwork.api.CombatDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DealDamageAction extends Action {

    private NumberArgument amount;
    private DamageArgument damage_source;

    private List<Action> on_hit = new ArrayList<>();
    private List<Action> on_damage = new ArrayList<>();
    private List<Action> on_kill = new ArrayList<>();

    @Override
    public int perform(MovesetWrapper wrapper, TimerAction parent, @Nullable Entity performer, Entity target) {
        CombatDamageSource baked = damage_source.bake(wrapper, parent, performer, target);
        boolean success = target.hurt(baked, (float) amount.resolve(wrapper, parent, performer, target));
        performer.getPersistentData().putFloat("combatcircle:finalized_damage", baked.getFinalDamage());
        int ret = runActions(wrapper, parent, on_hit, performer, target);
        if (success) {
            if(!target.isAlive()) {
                int damageRet = runActions(wrapper, parent, on_kill, performer, target);
                if (damageRet != 0) ret = damageRet;
            }
            int damageRet = runActions(wrapper, parent, on_damage, performer, target);
            if (damageRet != 0) ret = damageRet;
        }
        return ret;
    }
}
