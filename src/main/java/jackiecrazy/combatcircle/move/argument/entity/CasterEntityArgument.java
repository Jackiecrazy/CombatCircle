package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

public class CasterEntityArgument extends EntityArgument{
    public static final CasterEntityArgument INSTANCE=new CasterEntityArgument();
    @Override
    public Entity resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return caster;
    }
}
