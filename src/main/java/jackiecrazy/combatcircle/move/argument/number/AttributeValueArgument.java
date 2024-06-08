package jackiecrazy.combatcircle.move.argument.number;

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
    public double resolve(Entity caster, Entity target) {
        if (attr == null)
            attr = ForgeRegistries.ATTRIBUTES.getValue(attribute);
        if (attr != null && reference_point.resolveAsEntity(caster, target) instanceof LivingEntity le)
            return le.getAttributeValue(attr);
        return 0;
    }
}
