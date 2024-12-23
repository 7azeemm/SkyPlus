package com.hazem.skyplus.utils.schedular;

import com.mojang.brigadier.Command;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * A utility class for scheduling tasks.
 * Allows for synchronous, asynchronous, and cyclic task scheduling.
 */
public class Scheduler {
    private static final Scheduler INSTANCE = new Scheduler();

    private int currentTick = 0; // Keeps track of the current tick
    private final ObjectArrayList<ScheduledTask> tasks = new ObjectArrayList<>();

    public static Scheduler getInstance() {
        return INSTANCE;
    }

    /**
     * Opens a screen on the next tick.
     *
     * @param screenSupplier A supplier that provides the screen to open.
     */
    public static Command<FabricClientCommandSource> openScreen(Supplier<Screen> screenSupplier) {
        return context -> openScreen(screenSupplier.get());
    }

    private static int openScreen(Screen screen) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.send(() -> client.setScreen(screen));
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Schedules a one-time task to run after a delay synchronously.
     *
     * @param task  The task to run.
     * @param delay The delay in ticks before the task is executed.
     */
    public void schedule(Runnable task, int delay) {
        addTask(task, delay, 0, false, false);
    }

    /**
     * Schedules a one-time task to run after a delay asynchronously.
     *
     * @param task  The task to run.
     * @param delay The delay in ticks before the task is executed.
     */
    public void scheduleAsync(Runnable task, int delay) {
        addTask(task, delay, 0, false, true);
    }

    /**
     * Schedules a cyclic task to run at regular intervals synchronously.
     * The first execution is delayed by the given delay, and subsequent executions will occur at the specified interval.
     *
     * @param task          The task to run.
     * @param delay         The initial delay in ticks before the task is executed.
     * @param intervalTicks The interval in ticks between subsequent task executions.
     */
    public void scheduleCyclic(Runnable task, int delay, int intervalTicks) {
        addTask(task, delay, intervalTicks, true, false);
    }

    /**
     * Schedules a cyclic task to run at regular intervals asynchronously.
     * The first execution is delayed by the given delay, and subsequent executions will occur at the specified interval.
     *
     * @param task          The task to run.
     * @param delay         The initial delay in ticks before the task is executed.
     * @param intervalTicks The interval in ticks between subsequent task executions.
     */
    public void scheduleCyclicAsync(Runnable task, int delay, int intervalTicks) {
        addTask(task, delay, intervalTicks, true, true);
    }

    private void addTask(Runnable task, int delay, int intervalTicks, boolean cyclic, boolean async) {
        tasks.add(new ScheduledTask(task, intervalTicks, cyclic, async, currentTick + delay));
    }

    public void tick(MinecraftClient client) {
        currentTick++; // Increment the current tick counter

        Iterator<ScheduledTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            ScheduledTask task = iterator.next();

            // Check if the task is ready to run
            if (currentTick >= task.nextExecutionTick) {
                if (task.async) {
                    // Runs a task asynchronously (on a background thread).
                    client.executeAsync(future -> CompletableFuture.runAsync(task.runnable).whenComplete((result, exception) -> {
                        if (exception != null) {
                            future.completeExceptionally(exception);
                        } else {
                            future.complete(null); // Mark as completed
                        }
                    }));
                } else {
                    // Runs the task synchronously (on the main thread).
                    client.execute(task.runnable);
                }

                // Reschedule for the next cycle
                if (task.cyclic) tasks.add(task.next(currentTick));

                iterator.remove();
            }
        }
    }

    private record ScheduledTask(Runnable runnable, int intervalTicks, boolean cyclic, boolean async, int nextExecutionTick) {
        public ScheduledTask next(int currentTick) {
            // Create a new instance for the next cycle
            return new ScheduledTask(runnable, intervalTicks, cyclic, async, currentTick + intervalTicks);
        }
    }
}