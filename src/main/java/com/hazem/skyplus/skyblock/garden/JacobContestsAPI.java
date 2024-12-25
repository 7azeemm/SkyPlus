package com.hazem.skyplus.skyblock.garden;

import com.google.gson.JsonObject;
import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.utils.APIUtils;
import com.hazem.skyplus.utils.schedular.Scheduler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JacobContestsAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(JacobContestsAPI.class);
    private static final String JSON_URL = "https://api.elitebot.dev/contests/at/now";
    public static final long CONTEST_DURATION = Duration.ofMinutes(20).toMillis();
    public static final List<Contest> CONTESTS = new ArrayList<>();
    public static Contest activeContest;
    public static Contest nextContest;

    @Init
    public static void fetchContests() {
        APIUtils.fetchJson(JSON_URL).thenAccept(jsonObject -> {
            if (jsonObject == null) {
                LOGGER.error("Failed to get Jacob Contests data.");
                return;
            }

            JsonObject contestsJson = jsonObject.getAsJsonObject("contests");
            DataResult<Map<Long, List<String>>> mapResult = Codec.unboundedMap(Codec.STRING.xmap(Long::parseLong, String::valueOf), Codec.STRING.listOf()).parse(JsonOps.INSTANCE, contestsJson);
            mapResult.resultOrPartial(error -> LOGGER.error("Error parsing contests data: {}", error))
                    .ifPresent(map -> {
                        CONTESTS.clear();
                        map.forEach((time, crops) -> CONTESTS.add(new Contest(time * 1000, crops)));
                        getContestInfo();
                    });
        }).exceptionally(ex -> {
            LOGGER.error("Exception while fetching contests data", ex);
            return null;
        });
    }

    public static void getContestInfo() {
        long now = System.currentTimeMillis();
        boolean nextContestFound = false;
        for (Contest contest : CONTESTS) {
            long startTime = contest.time();
            if (now < startTime && startTime - now < CONTEST_DURATION * 3) {
                nextContestFound = true;
                nextContest = contest;
                Scheduler.getInstance().schedule(JacobContestsAPI::getContestInfo, (int) ((startTime - now) / 50));
            } else if (now >= startTime && now < startTime + CONTEST_DURATION) {
                activeContest = contest;
            }
        }
        if (!nextContestFound) Scheduler.getInstance().schedule(JacobContestsAPI::fetchContests, 20 * 60 * 45);
    }

    public record Contest(long time, List<String> crops) { }
}