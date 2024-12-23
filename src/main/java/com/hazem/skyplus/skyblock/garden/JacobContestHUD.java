package com.hazem.skyplus.skyblock.garden;

import com.hazem.skyplus.annotations.Widget;
import com.hazem.skyplus.config.ConfigManager;
import com.hazem.skyplus.utils.HypixelData;
import com.hazem.skyplus.utils.Location;
import com.hazem.skyplus.utils.hud.AbstractWidget;
import com.hazem.skyplus.utils.hud.components.ComponentBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Widget
public class JacobContestHUD extends AbstractWidget {
    private static final long CONTEST_DURATION_MILLIS = Duration.ofMinutes(20).toMillis();

    @Override
    protected boolean shouldRender() {
        return ConfigManager.getHandler().garden.jacobContest.enableHUD && (ConfigManager.getHandler().garden.jacobContest.enableOutsideGarden ? HypixelData.isInSkyblock : HypixelData.isInIsland(Location.GARDEN));
    }

    @Override
    protected boolean shouldDrawBackground() {
        return ConfigManager.getHandler().garden.jacobContest.enableBackgroundDisplay;
    }

    @Override
    protected void update() {
        long now = Instant.now().toEpochMilli();

        if (JacobContestsAPI.activeContest != null) {
            long timeLeftMillis = JacobContestsAPI.activeContest.time() + CONTEST_DURATION_MILLIS - now;
            renderContest(Text.literal("Active:").formatted(Formatting.GREEN), timeLeftMillis, JacobContestsAPI.activeContest.crops());
        }

        if (JacobContestsAPI.nextContest != null) {
            long timeUntilStartMillis = JacobContestsAPI.nextContest.time() - now;
            renderContest(Text.literal("Next:").formatted(Formatting.GREEN), timeUntilStartMillis, JacobContestsAPI.nextContest.crops());
        }
    }

    private void renderContest(Text label, long timeMillis, List<String> crops) {
        String formattedTime = formatTime(timeMillis);
        addComponent(new ComponentBuilder()
                .addText(label)
                .addIcon(CropType.fromCropName(crops.getFirst()).itemStack)
                .addIcon(CropType.fromCropName(crops.get(1)).itemStack)
                .addIcon(CropType.fromCropName(crops.get(2)).itemStack)
                .addText(Text.of(" "))
                .addText(Text.literal("(").formatted(Formatting.GRAY))
                .addText(Text.literal(formattedTime).formatted(Formatting.YELLOW))
                .addText(Text.literal(")").formatted(Formatting.GRAY))
        );
    }

    private String formatTime(long millis) {
        if (millis <= 0 && JacobContestsAPI.activeContest != null) JacobContestsAPI.activeContest = null;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return minutes != 0 ? minutes + "m " + seconds + "s" : seconds + "s";
    }

    @Override
    protected int getX() {
        return 960;
    }

    @Override
    protected int getY() {
        return 0;
    }
}