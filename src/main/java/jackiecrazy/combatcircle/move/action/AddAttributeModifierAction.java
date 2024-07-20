package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AddAttributeModifierAction extends Action {
    private Argument<ResourceLocation> attribute;
    private transient Attribute attr;
    private Argument<Double> amount;
    private AttributeModifier.Operation operation;
    private String name;
    private UUID uuid;
    private Argument<Entity> recipient = CasterEntityArgument.INSTANCE;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        if (attr == null) attr = ForgeRegistries.ATTRIBUTES.getValue(attribute.resolve(wrapper, parent, performer, target));
        if (attr != null && recipient.resolve(wrapper, parent, performer, target) instanceof LivingEntity ent && ent.getAttribute(attr) != null) {
            ent.getAttribute(attr).removeModifier(uuid);
            AttributeModifier am = new AttributeModifier(uuid, name, amount.resolve(wrapper, parent, performer, target), operation);
            ent.getAttribute(attr).addTransientModifier(am);
        }
        return 0;
    }
}
