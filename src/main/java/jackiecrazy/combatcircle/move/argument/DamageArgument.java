package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.stack.EquippedItemArgument;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.FalseCondition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import jackiecrazy.footwork.api.CombatDamageSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DamageArgument implements Argument<DamageSource> {
    private Argument<Entity> source = new CasterEntityArgument();
    private Argument<Entity> proxy = new CasterEntityArgument();
    private CombatDamageSource.TYPE typing = CombatDamageSource.TYPE.PHYSICAL;
    private Argument<ItemStack> equip = new EquippedItemArgument();
    private Argument<Double> crit_damage = new FixedNumberArgument(1.5);
    private Argument<Double> posture_damage = new FixedNumberArgument(-1);
    private Argument<Double> armor_pierce_percentage = new FixedNumberArgument(0);
    private Argument<Double> knockback_percentage = new FixedNumberArgument(1);
    private Argument<Double> damage_multiplier = new FixedNumberArgument(1);
    private Condition crit = new FalseCondition();
    private Condition proc_normal = new TrueCondition();
    private Condition proc_attack = new TrueCondition();
    private Condition proc_skill = new FalseCondition();
    private List<ResourceLocation> tags = new ArrayList<>();
    transient Set<TagKey<DamageType>> dtags;

    public DamageSource resolve(MovesetWrapper wrapper, Action parent, @Nullable Entity caster, Entity target) {
        if (dtags == null) {
            dtags = tags.stream().map(a -> TagKey.create(Registries.DAMAGE_TYPE, a)).collect(Collectors.toSet());
        }
        CombatDamageSource ret = new CombatDamageSource(source.resolve(wrapper, parent, caster, target))
                .setDamageDealer(equip.resolve(wrapper, parent, caster, target))
                .setProxy(proxy.resolve(wrapper, parent, caster, target))
                .setDamageTyping(typing)
                .setProcAttackEffects(proc_attack.resolve(wrapper, parent, caster, target))
                .setProcSkillEffects(proc_skill.resolve(wrapper, parent, caster, target))
                .setProcNormalEffects(proc_normal.resolve(wrapper, parent, caster, target))
                .setCrit(crit.resolve(wrapper, parent, caster, target))
                .setCritDamage(crit_damage.resolve(wrapper, parent, caster, target).floatValue())
                .setArmorReductionPercentage(armor_pierce_percentage.resolve(wrapper, parent, caster, target).floatValue())
                .setKnockbackPercentage(knockback_percentage.resolve(wrapper, parent, caster, target).floatValue())
                .setMultiplier(damage_multiplier.resolve(wrapper, parent, caster, target).floatValue())
                .setPostureDamage(posture_damage.resolve(wrapper, parent, caster, target).floatValue());
        dtags.forEach(ret::flag);
        return ret;
    }
}
