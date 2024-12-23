package com.hazem.skyplus.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.scoreboard.Scoreboard;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Scoreboard.class)
public class ScoreboardMixin {

    @WrapWithCondition(
            method = "addTeam",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"
            )
    )
    private boolean suppressTeamWarning(Logger logger, String message, Object param) {
        return false;
    }
}