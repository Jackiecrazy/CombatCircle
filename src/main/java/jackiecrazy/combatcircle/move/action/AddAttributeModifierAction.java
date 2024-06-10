package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Supplier;

public class AddAttributeModifierAction extends Action {
    private ResourceLocation attribute;
    private transient Attribute attr;
    private NumberArgument amount;
    private AttributeModifier.Operation operation;
    private String name;
    private UUID uuid;
    private EntityArgument recipient = CasterEntityArgument.INSTANCE;

    @Override
    public int perform(MovesetWrapper wrapper, @Nullable TimerAction parent, Entity performer, Entity target) {
        if (attr == null) attr = ForgeRegistries.ATTRIBUTES.getValue(attribute);
        if (attr != null && recipient.resolveAsEntity(performer, target) instanceof LivingEntity ent && ent.getAttribute(attr) != null) {
            ent.getAttribute(attr).removeModifier(uuid);
            AttributeModifier am = new AttributeModifier(uuid, name, amount.resolve(performer, target), operation);
            ent.getAttribute(attr).addTransientModifier(am);
        }
        return 0;
    }
}
