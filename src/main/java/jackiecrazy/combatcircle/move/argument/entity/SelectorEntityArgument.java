package jackiecrazy.combatcircle.move.argument.entity;

import jackiecrazy.combatcircle.move.argument.SelectorArgument;
import net.minecraft.world.entity.Entity;

public class SelectorEntityArgument extends EntityArgument{
    private SelectorArgument select;
    @Override
    public Entity resolve(Entity caster, Entity target) {
        return select.resolve(caster, target).get(0);
    }
}
