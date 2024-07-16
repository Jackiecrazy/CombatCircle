package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class RandomNumberArgument extends NumberArgument{
    NumberArgument bound;
    @Override
    public double resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
        return CombatCircle.rand.nextDouble(bound.resolve(wrapper, parent, caster, target));
    }
}
