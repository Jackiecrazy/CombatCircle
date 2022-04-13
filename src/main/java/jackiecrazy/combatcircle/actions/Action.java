package jackiecrazy.combatcircle.actions;

import com.google.gson.TypeAdapter;
import jackiecrazy.combatcircle.CombatCircle;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public abstract class Action{
    public abstract void perform(LivingEntity performer, LivingEntity target);
}
