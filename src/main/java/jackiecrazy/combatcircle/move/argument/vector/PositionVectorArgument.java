package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PositionVectorArgument extends VectorArgument {
    private EntityArgument reference_point= CasterEntityArgument.INSTANCE;
    public static final PositionVectorArgument CASTER = new PositionVectorArgument();

    @Override
    public Vec3 _resolve(Entity caster, Entity target) {
        return reference_point.resolveAsEntity(caster, target).position();
    }
}
