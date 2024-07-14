package jackiecrazy.combatcircle.move.argument.stack;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RawItemStackArgument extends ItemStackArgument{
    private ResourceLocation item;
    private NumberArgument count = NumberArgument.ZERO;
    private CompoundTag tag;

    public ItemStack resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        ItemStack ret = new ItemStack(ForgeRegistries.ITEMS.getValue(item));
        ret.setCount((int) Math.max(count.resolve(wrapper, caster, target), 1));
        if (tag != null)
            ret.setTag(tag.copy());
        return ret;
    }
}
