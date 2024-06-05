package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class LookVectorArgument extends VectorArgument{
    public LookVectorArgument(EntityArgument looker) {
        this.looker = looker;
    }

    EntityArgument looker;

    @Override
    Vec3 _resolve(Entity caster, Entity target) {
        return looker.resolve(caster, target).getLookAngle();
    }
}
