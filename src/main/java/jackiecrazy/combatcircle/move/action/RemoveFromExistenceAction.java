package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class RemoveFromExistenceAction extends Action {
    private Argument<Entity> entity = TargetEntityArgument.INSTANCE;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        entity.resolve(wrapper, parent, performer, target).remove(Entity.RemovalReason.KILLED);
        return 0;
    }
}
