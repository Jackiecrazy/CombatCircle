package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class SwingArmAction extends Action {
    private InteractionHand hand;
    private Argument<Entity> swinger = CasterEntityArgument.INSTANCE;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        if (swinger.resolve(wrapper, parent, performer, target) instanceof LivingEntity e) {
            e.swing(hand, true);
            e.level().broadcastEntityEvent(e, (byte)4);
        }
        return 0;
    }
}
