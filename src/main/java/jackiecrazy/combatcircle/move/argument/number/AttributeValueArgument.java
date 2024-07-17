package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeValueArgument implements Argument<Double> {
    private Argument<Entity> reference_point;
    private ResourceLocation attribute;
    private Attribute attr;

    @Override
    public Double resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        if (attr == null)
            attr = ForgeRegistries.ATTRIBUTES.getValue(attribute);
        if (attr != null && reference_point.resolve(wrapper, parent, caster, target) instanceof LivingEntity le)
            return le.getAttributeValue(attr);
        return attr.getDefaultValue();
    }
}
