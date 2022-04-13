package jackiecrazy.combatcircle.ai;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import jackiecrazy.combatcircle.conditions.Condition;
import jackiecrazy.combatcircle.conditions.NoCondition;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class MoveSet {
    public static final MoveSet DEFAULT = new MoveSet(new NoCondition(), 0, 0, 0, 0, new ArrayList<>());

    public static final Codec<MoveSet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GearConfigAttributeModifier.CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(ArmorGearConfig::getAttributes),
            ENCHANTMENT_DATA_CODEC.listOf().optionalFieldOf("built_in_enchantments", new ArrayList<>()).forGetter(ArmorGearConfig::getBuiltInEnchantments),
            ResourceLocation.CODEC.fieldOf("armor_material").forGetter(armorGearConfig -> armorGearConfig.materialResource),
            Codec.BOOL.optionalFieldOf("unique", false).forGetter(ArmorGearConfig::isUnique),
            ITEM_RARITY_CODEC.fieldOf("rarity").forGetter(ArmorGearConfig::getRarity)
    ).apply(instance, MoveSet::new));

    public MoveSet(Condition condition, int cooldown, int priority, int weight, int forceAbandonTime, ArrayList<Move> sequence){

    }
}
