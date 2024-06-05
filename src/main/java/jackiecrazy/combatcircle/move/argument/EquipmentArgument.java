package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EquipmentArgument extends Argument {
    private EquipmentSlot slot;

    public EquipmentSlot getSlot() {
        return slot;
    }

    private EntityArgument entity;

    public EquipmentArgument(EquipmentSlot slot, EntityArgument entity) {
        this.slot = slot;
        this.entity = entity;
    }

    public ItemStack resolve(Entity caster, Entity target) {
        Entity e = entity.resolve(caster, target);
        if (e instanceof LivingEntity ent) return ent.getItemBySlot(slot);
        return ItemStack.EMPTY;
    }
}
