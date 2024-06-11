package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeValueArgument extends NumberArgument {
    private EntityArgument reference_point;
    private ResourceLocation attribute;
    private Attribute attr;

    @Override
    public double resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
        if (attr == null)
            attr = ForgeRegistries.ATTRIBUTES.getValue(attribute);
        if (attr != null && reference_point.resolveAsEntity(wrapper, parent, caster, target) instanceof LivingEntity le)
            return le.getAttributeValue(attr);
        return 0;
    }
}
