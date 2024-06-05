package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;

public abstract class EntityArgument extends Argument {
    public abstract Entity resolve(Entity caster, Entity target);
}
