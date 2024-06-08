package jackiecrazy.combatcircle.move.argument.entity;

import net.minecraft.world.entity.Entity;

public class CasterEntityArgument extends EntityArgument{
    public static final CasterEntityArgument INSTANCE=new CasterEntityArgument();
    @Override
    public Entity resolveAsEntity(Entity caster, Entity target) {
        return caster;
    }
}
