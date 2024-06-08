package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CurrentHealthArgument extends NumberArgument {
    private EntityArgument reference_point;

    @Override
    public double resolve(Entity caster, Entity target) {
        return reference_point.resolveAsEntity(caster, target) instanceof LivingEntity le ? le.getHealth() : 0;
    }
}
