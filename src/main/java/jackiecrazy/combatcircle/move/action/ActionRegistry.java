package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.CombatCircle;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ActionRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "actions");
    public static Supplier<IForgeRegistry<Action>> SUPPLIER;
    public static DeferredRegister<Action> ACTIONS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);
}
