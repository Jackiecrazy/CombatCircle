package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.CombatCircle;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ConditionRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "conditions");
    public static Supplier<IForgeRegistry<Condition>> SUPPLIER;
    public static DeferredRegister<Condition> ACTIONS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);
}
