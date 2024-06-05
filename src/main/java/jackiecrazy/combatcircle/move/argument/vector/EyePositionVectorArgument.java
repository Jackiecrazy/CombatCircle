package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EyePositionVectorArgument extends VectorArgument {
    private EntityArgument reference_point;

    public EyePositionVectorArgument(EntityArgument referencePoint) {
        reference_point = referencePoint;
    }

    @Override
    public Vec3 _resolve(Entity caster, Entity target) {
        return reference_point.resolve(caster, target).getEyePosition();
    }
}
