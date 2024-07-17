package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class IsTaggedWithCondition extends Condition {
    private Argument<Entity> reference = TargetEntityArgument.INSTANCE;
    private transient TagKey<EntityType<?>> tagg;
    private ResourceLocation tag;

    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        if (tagg == null) tagg = new TagKey<>(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), tag);
        Entity ref = reference.resolve(wrapper, parent, performer, target);
        return ref.getType().is(tagg);
    }
}
