package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.CombatCircle;
import net.minecraft.world.entity.Entity;

public class RandomNumberArgument extends NumberArgument{
    NumberArgument bound;
    @Override
    public double resolve(Entity caster, Entity target) {
        return CombatCircle.rand.nextDouble(bound.resolve(caster, target));
    }
}
