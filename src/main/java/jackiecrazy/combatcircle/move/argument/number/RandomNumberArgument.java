package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;

public class RandomNumberArgument extends NumberArgument{
    Argument<Double> bound;
    @Override
    public Double resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return CombatCircle.rand.nextDouble(bound.resolve(wrapper, parent, caster, target));
    }
}
