package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.DamageArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
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
    public int perform(MovesetWrapper wrapper, @Nullable TimerAction parent, Entity performer, Entity target) {
        int ret = runActions(wrapper, parent, on_hit, performer, target);
        if (target.hurt(damage_source.bake(wrapper, parent, performer, target), (float) amount.resolve(wrapper, parent, performer, target))) {
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
