package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.resources.ResourceLocation;

public class ComponentAction extends TimerAction {
    private ResourceLocation component;
    private transient TimerAction referent;


}
