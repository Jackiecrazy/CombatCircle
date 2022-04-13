package jackiecrazy.combatcircle.move;

import jackiecrazy.combatcircle.actions.Action;

public class Move {

    //public static final Move DEFAULT=new Move()

//    public static final Codec<MoveSet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            GearConfigAttributeModifier.CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(ArmorGearConfig::getAttributes),
//            ENCHANTMENT_DATA_CODEC.listOf().optionalFieldOf("built_in_enchantments", new ArrayList<>()).forGetter(ArmorGearConfig::getBuiltInEnchantments),
//            ResourceLocation.CODEC.fieldOf("armor_material").forGetter(armorGearConfig -> armorGearConfig.materialResource),
//            Codec.BOOL.optionalFieldOf("unique", false).forGetter(ArmorGearConfig::isUnique),
//            ITEM_RARITY_CODEC.fieldOf("rarity").forGetter(ArmorGearConfig::getRarity)
//    ).apply(instance, MoveSet::new));

    public Move(Move inherit, Action initiation, Action p){

    }
}
