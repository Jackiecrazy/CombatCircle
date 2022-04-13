package jackiecrazy.combatcircle.conditions;

import jackiecrazy.combatcircle.CombatCircle;
import net.minecraftforge.registries.DeferredRegister;

public class Conditions {
    public static DeferredRegister<Condition> CONDITIONS = DeferredRegister
            .create(Condition.class, CombatCircle.MODID);
}
