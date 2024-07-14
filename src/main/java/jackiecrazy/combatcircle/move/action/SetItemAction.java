package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.stack.ItemStackArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SetItemAction extends Action {
    private EntityArgument wielder = CasterEntityArgument.INSTANCE;
    private ItemStackArgument stack;
    private EquipmentSlot slot;

    @Override
    public int perform(MovesetWrapper wrapper, @Nullable Entity performer, Entity target) {
        if (wielder.resolveAsEntity(wrapper, performer, target) instanceof LivingEntity e)
            e.setItemSlot(slot, stack.resolve(wrapper, performer, target).copy());
        return 0;
    }
}
