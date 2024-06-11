package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.SelectorArgument;
import net.minecraft.world.entity.Entity;

public class SelectorEntityArgument extends EntityArgument{
    private SelectorArgument select;
    @Override
    public Entity resolveAsEntity(MovesetWrapper wrapper, Entity caster, Entity target) {
        return select.resolve(wrapper, caster, target).get(0);
    }
}
