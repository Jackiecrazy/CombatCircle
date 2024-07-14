package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.registries.ForgeRegistries;

public class IsTypeCondition extends Condition {
    private EntityArgument reference = TargetEntityArgument.INSTANCE;
    private MobType type;//fixme not serializable

    @Override
    public boolean evaluate(MovesetWrapper wrapper, Entity performer, Entity target) {
        Entity ref = reference.resolveAsEntity(wrapper, performer, target);
        return ref instanceof Mob mob &&mob.getMobType().equals(type);
    }
}
