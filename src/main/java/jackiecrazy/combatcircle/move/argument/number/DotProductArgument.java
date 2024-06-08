package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.world.entity.Entity;

public class DotProductArgument extends NumberArgument {
    private VectorArgument first, second;

    @Override
    public double resolve(Entity caster, Entity target) {
        return first.resolve(caster, target).dot(second.resolve(caster, target));
    }
}
