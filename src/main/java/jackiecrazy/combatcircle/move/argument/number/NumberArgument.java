package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;

public abstract class NumberArgument extends Argument {
    public static final FixedNumberArgument ZERO=new FixedNumberArgument(0);
    public static final FixedNumberArgument ONE=new FixedNumberArgument(1);
    public abstract double resolve(Entity caster, Entity target);
}
