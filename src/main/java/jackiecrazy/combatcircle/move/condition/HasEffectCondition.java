package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class HasEffectCondition extends Condition {
    private ResourceLocation effect;
    private transient MobEffect me;
    private Argument<Double> minimum_potency= FixedNumberArgument.ZERO, minimum_duration= FixedNumberArgument.ZERO;
    private Argument<Entity> tested = TargetEntityArgument.INSTANCE;

    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        if (me == null)
            me = ForgeRegistries.MOB_EFFECTS.getValue(effect);
        if (me != null && tested.resolve(wrapper, parent, performer, target) instanceof LivingEntity e) {
            MobEffectInstance inst = e.getEffect(me);
            if (inst != null) {
                return inst.getDuration() > (minimum_duration.resolve(wrapper, parent, performer, target)) && inst.getAmplifier() > minimum_potency.resolve(wrapper, parent, performer, target);
            }
        }
        return false;
    }
}
