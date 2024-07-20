package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class IsTaggedWithCondition {
    public static class Mob extends Condition {
        private Argument<Entity> reference = TargetEntityArgument.INSTANCE;
        private transient TagKey<EntityType<?>> tagg;
        private Argument<ResourceLocation> tag;

        @Override
        public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
            if (tagg == null) tagg = new TagKey<>(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), tag.resolve(wrapper, parent, performer, target));
            Entity ref = reference.resolve(wrapper, parent, performer, target);
            return ref.getType().is(tagg);
        }
    }
    public static class Damage extends Condition {
        private Argument<DamageSource> reference;
        private transient TagKey<DamageType> tagg;
        private Argument<ResourceLocation> tag;

        @Override
        public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
            if (tagg == null) tagg = new TagKey<>(Registries.DAMAGE_TYPE, tag.resolve(wrapper, parent, performer, target));
            DamageSource ref = reference.resolve(wrapper, parent, performer, target);
            return ref.is(tagg);
        }
    }
    public static class Stack extends Condition {
        private Argument<ItemStack> reference;
        private transient TagKey<Item> tagg;
        private Argument<ResourceLocation> tag;

        @Override
        public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
            if (tagg == null) tagg = new TagKey<>(ForgeRegistries.ITEMS.getRegistryKey(), tag.resolve(wrapper, parent, performer, target));
            ItemStack ref = reference.resolve(wrapper, parent, performer, target);
            return ref.is(tagg);
        }
    }
    public static class Bloc extends Condition {
        private Argument<BlockState> reference;
        private transient TagKey<Block> tagg;
        private Argument<ResourceLocation> tag;

        @Override
        public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
            if (tagg == null) tagg = new TagKey<>(ForgeRegistries.BLOCKS.getRegistryKey(), tag.resolve(wrapper, parent, performer, target));
            BlockState ref = reference.resolve(wrapper, parent, performer, target);
            return ref.is(tagg);
        }
    }
}
