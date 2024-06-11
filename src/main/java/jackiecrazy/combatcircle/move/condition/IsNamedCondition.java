package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;

public class IsNamedCondition extends Condition {
    private EntityArgument reference= CasterEntityArgument.INSTANCE;
    private String name;
    @Override
    public boolean evaluate(MovesetWrapper wrapper, Entity performer, Entity target) {
        Entity ref=reference.resolveAsEntity(wrapper, performer, target);
        return ref.hasCustomName()&&ref.getCustomName().getString().equals(name);
    }
}
