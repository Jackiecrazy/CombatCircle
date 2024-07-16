package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;

public class IsTypeCondition extends Condition {
    private EntityArgument reference = TargetEntityArgument.INSTANCE;
    private MobType type;//fixme not serializable

    @Override
    public boolean resolve(MovesetWrapper wrapper, TimerAction parent, Entity performer, Entity target) {
        Entity ref = reference.resolveAsEntity(wrapper, parent, performer, target);
        return ref instanceof Mob mob &&mob.getMobType().equals(type);
    }
}
