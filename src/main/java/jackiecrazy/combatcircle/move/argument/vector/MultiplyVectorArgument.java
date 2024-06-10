package jackiecrazy.combatcircle.move.argument.vector;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MultiplyVectorArgument extends VectorArgument {
    VectorArgument base;
    VectorArgument multiply;

    @Override
    public Vec3 _resolve(Entity caster, Entity target) {
        return base.resolveAsVector(caster, target).multiply(multiply.resolveAsVector(caster, target));
    }
}
