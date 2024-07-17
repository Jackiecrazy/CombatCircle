package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class IsTargetCondition extends Condition {
    private Argument<Entity> reference= CasterEntityArgument.INSTANCE;
    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        Entity ref=reference.resolve(wrapper, parent, performer, target);
        return ref instanceof Mob mob && mob.getTarget() == target;
    }
}
