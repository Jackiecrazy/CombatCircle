package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class AddEffectAction extends Action {
    private ResourceLocation effect;
    private transient MobEffect me;
    private Argument<Double> potency, duration;
    private Argument<Entity> recipient= TargetEntityArgument.INSTANCE;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        if (me == null)
            me = ForgeRegistries.MOB_EFFECTS.getValue(effect);
        if (me != null && recipient.resolve(wrapper, parent, performer, target) instanceof LivingEntity e) {
            e.addEffect(new MobEffectInstance(me, duration.resolve(wrapper, parent, performer, target).intValue(), potency.resolve(wrapper, parent, performer, target).intValue()));
        }
        return 0;
    }
}
