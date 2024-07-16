package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class IsTaggedWithCondition extends Condition {
    private EntityArgument reference = TargetEntityArgument.INSTANCE;
    private transient TagKey<EntityType<?>> tagg;
    private ResourceLocation tag;

    @Override
    public boolean resolve(MovesetWrapper wrapper, Entity performer, Entity target) {
        if (tagg == null) tagg = new TagKey<>(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), tag);
        Entity ref = reference.resolveAsEntity(wrapper, performer, target);
        return ref.getType().is(tagg);
    }
}
