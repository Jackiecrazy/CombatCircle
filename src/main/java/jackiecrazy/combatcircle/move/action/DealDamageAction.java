package jackiecrazy.combatcircle.move.action;

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

    @Override
    public int perform(@Nullable TimerAction parent, Entity performer, Entity target) {
        int ret = runActions(parent, on_hit, performer, target);
        if (target.hurt(damage_source.bake(parent, performer, target), (float) amount.resolve(performer, target))) {
            int damageRet = runActions(parent, on_damage, performer, target);
            if (damageRet != 0) ret = damageRet;
        }
        return ret;
    }
}
