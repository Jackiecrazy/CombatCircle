package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PositionVectorArgument extends VectorArgument {
    private Argument<Entity> reference_point= CasterEntityArgument.INSTANCE;
    public static final PositionVectorArgument CASTER = new PositionVectorArgument();

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return reference_point.resolve(wrapper, parent, caster, target).position();
    }
}
