package jackiecrazy.combatcircle.move.argument.resourcelocation;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryNameArgument extends ResourceLocationArgument {
    Argument<?> of;

    @Override
    public ResourceLocation resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        Object obj = of.resolve(wrapper, parent, caster, target);
        //awful everything
        if (obj instanceof Item i) return ForgeRegistries.ITEMS.getKey(i);
        if (obj instanceof ItemStack i) return ForgeRegistries.ITEMS.getKey(i.getItem());
        if (obj instanceof EntityType<?> i) return ForgeRegistries.ENTITY_TYPES.getKey(i);
        if (obj instanceof Entity i) return ForgeRegistries.ENTITY_TYPES.getKey(i.getType());
        if (obj instanceof MobEffect i) return ForgeRegistries.MOB_EFFECTS.getKey(i);
        if (obj instanceof MobEffectInstance i) return ForgeRegistries.MOB_EFFECTS.getKey(i.getEffect());
        if (obj instanceof Block i) return ForgeRegistries.BLOCKS.getKey(i);
        if (obj instanceof BlockState i) return ForgeRegistries.BLOCKS.getKey(i.getBlock());
        if (obj instanceof SoundEvent i) return ForgeRegistries.SOUND_EVENTS.getKey(i);
        return null;
    }
}
