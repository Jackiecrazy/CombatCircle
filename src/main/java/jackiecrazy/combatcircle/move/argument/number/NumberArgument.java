package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;

public abstract class NumberArgument extends Argument {
    public abstract double resolve(Entity caster, Entity target);
}
