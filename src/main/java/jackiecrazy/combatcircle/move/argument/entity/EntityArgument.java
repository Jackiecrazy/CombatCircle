package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public abstract class EntityArgument extends VectorArgument {
    public abstract Entity resolveAsEntity(Entity caster, Entity target);

    public Vec3 _resolve(Entity caster, Entity target) {
        return resolveAsEntity(caster, target).position();
    }
}
