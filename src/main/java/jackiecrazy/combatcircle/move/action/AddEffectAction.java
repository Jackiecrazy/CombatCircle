package jackiecrazy.combatcircle.move.action;

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

public class AddEffectAction extends Action {
    private ResourceLocation effect;
    private transient MobEffect me;
    private NumberArgument potency, duration;
    private EntityArgument recipient= TargetEntityArgument.INSTANCE;

    @Override
    public int perform(MovesetWrapper wrapper, TimerAction parent, @Nullable Entity performer, Entity target) {
        if (me == null)
            me = ForgeRegistries.MOB_EFFECTS.getValue(effect);
        if (me != null && recipient.resolveAsEntity(wrapper, parent, performer, target) instanceof LivingEntity e) {
            e.addEffect(new MobEffectInstance(me, (int) (duration.resolve(wrapper, parent, performer, target)), (int) potency.resolve(wrapper, parent, performer, target)));
        }
        return 0;
    }
}
