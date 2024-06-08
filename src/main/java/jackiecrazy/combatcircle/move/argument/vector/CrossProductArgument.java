package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class CrossProductArgument extends VectorArgument {
    private VectorArgument first, second;

    @Override
    public Vec3 _resolve(Entity caster, Entity target) {
        return first.resolve(caster, target).cross(second.resolve(caster, target));
    }
}
