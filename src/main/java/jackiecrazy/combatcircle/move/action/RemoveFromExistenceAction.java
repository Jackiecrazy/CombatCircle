package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class RemoveFromExistenceAction extends Action {
    private EntityArgument entity = TargetEntityArgument.INSTANCE;

    @Override
    public int perform(MovesetWrapper wrapper, TimerAction parent, @Nullable Entity performer, Entity target) {
        entity.resolveAsEntity(wrapper, parent, performer, target).remove(Entity.RemovalReason.KILLED);
        return 0;
    }
}
