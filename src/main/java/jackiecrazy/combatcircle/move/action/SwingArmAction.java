package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class SwingArmAction extends Action {
    private InteractionHand hand;
    private EntityArgument swinger = CasterEntityArgument.INSTANCE;

    @Override
    public int perform(MovesetWrapper wrapper, @Nullable Entity performer, Entity target) {
        if (swinger.resolveAsEntity(wrapper, performer, target) instanceof LivingEntity e) {
            e.swing(hand, true);
            e.level().broadcastEntityEvent(e, (byte)4);
        }
        return 0;
    }
}
