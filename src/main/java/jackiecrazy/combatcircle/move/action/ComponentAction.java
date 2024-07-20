package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.resources.ResourceLocation;

public class ComponentAction extends TimerAction {
    private Argument<ResourceLocation> component;
    private transient TimerAction referent;


}
