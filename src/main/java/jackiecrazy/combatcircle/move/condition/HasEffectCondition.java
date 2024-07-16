package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
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
    private NumberArgument minimum_potency=NumberArgument.ZERO, minimum_duration=NumberArgument.ZERO;
    private EntityArgument tested = TargetEntityArgument.INSTANCE;

    @Override
    public boolean resolve(MovesetWrapper wrapper, @Nullable Entity performer, Entity target) {
        if (me == null)
            me = ForgeRegistries.MOB_EFFECTS.getValue(effect);
        if (me != null && tested.resolveAsEntity(wrapper, performer, target) instanceof LivingEntity e) {
            MobEffectInstance inst = e.getEffect(me);
            if (inst != null) {
                return inst.getDuration() > (minimum_duration.resolve(wrapper, performer, target)) && inst.getAmplifier() > minimum_potency.resolve(wrapper, performer, target);
            }
        }
        return false;
    }
}
