package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.FalseCondition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import jackiecrazy.footwork.api.CombatDamageSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DamageArgument extends Argument {
    private EntityArgument source = new CasterEntityArgument();
    private EntityArgument proxy = new CasterEntityArgument();
    private CombatDamageSource.TYPE typing = CombatDamageSource.TYPE.PHYSICAL;
    private EquipmentArgument equip = new EquipmentArgument(EquipmentSlot.MAINHAND, source);
    private NumberArgument crit_damage = new FixedNumberArgument(1.5);
    private NumberArgument posture_damage = new FixedNumberArgument(-1);
    private NumberArgument armor_pierce_percentage = new FixedNumberArgument(0);
    private NumberArgument knockback_percentage = new FixedNumberArgument(1);
    private NumberArgument damage_multiplier = new FixedNumberArgument(1);
    private Condition crit = new FalseCondition();
    private Condition proc_normal = new TrueCondition();
    private Condition proc_attack = new TrueCondition();
    private Condition proc_skill = new FalseCondition();
    private List<ResourceLocation> tags = new ArrayList<>();
    transient Set<TagKey<DamageType>> dtags;

    public CombatDamageSource bake(MovesetWrapper wrapper, @Nullable Entity caster, Entity target) {
        if (dtags == null) {
            dtags = tags.stream().map(a -> TagKey.create(Registries.DAMAGE_TYPE, a)).collect(Collectors.toSet());
        }
        CombatDamageSource ret = new CombatDamageSource(source.resolveAsEntity(wrapper, caster, target))
                .setDamageDealer(equip.resolve(wrapper, caster, target))
                .setProxy(proxy.resolveAsEntity(wrapper, caster, target))
                .setDamageTyping(typing)
                .setProcAttackEffects(proc_attack.evaluate(wrapper, caster, target))
                .setProcSkillEffects(proc_skill.evaluate(wrapper, caster, target))
                .setProcNormalEffects(proc_normal.evaluate(wrapper, caster, target))
                .setCrit(crit.evaluate(wrapper, caster, target))
                .setCritDamage((float) crit_damage.resolve(wrapper, caster, target))
                .setArmorReductionPercentage((float) armor_pierce_percentage.resolve(wrapper, caster, target))
                .setKnockbackPercentage((float) knockback_percentage.resolve(wrapper, caster, target))
                .setMultiplier((float) damage_multiplier.resolve(wrapper, caster, target))
                .setAttackingHand(equip.getSlot().getType() == EquipmentSlot.Type.HAND ? equip.getSlot() == EquipmentSlot.MAINHAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND : null)
                .setPostureDamage((float) posture_damage.resolve(wrapper, caster, target));
        dtags.forEach(ret::flag);
        return ret;
    }
}
