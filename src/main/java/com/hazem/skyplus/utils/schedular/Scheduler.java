package com.hazem.skyplus.utils.schedular;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * A utility class for scheduling tasks.
 * Allows for synchronous, asynchronous, and cyclic task scheduling.
 */
public class Scheduler {
    private static final Scheduler INSTANCE = new Scheduler();

    private int currentTick = 0; // Keeps track of the current tick
    private final List<ScheduledTask> tasks = new ArrayList<>();

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
     * Schedules a cyclic task to run at regular intervals.
     *
     * @param task         The task to run.
     * @param intervalTicks The interval in ticks between task executions.
     * @param async        Whether the task should run asynchronously.
     */
    public void scheduleCyclic(Runnable task, int intervalTicks, boolean async) {
        addTask(task, intervalTicks, true, async);
    }

    /**
     * Schedules a one-time task to run after a delay.
     *
     * @param task  The task to run.
     * @param delay The delay in ticks before the task is executed.
     * @param async Whether the task should run asynchronously.
     */
    public void schedule(Runnable task, int delay, boolean async) {
        addTask(task, delay, false, async);
    }

    private void addTask(Runnable task, int intervalTicks, boolean cyclic, boolean async) {
        tasks.add(new ScheduledTask(task, intervalTicks, cyclic, async));
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

                if (task.cyclic) {
                    // Reschedule for the next cycle
                    task.nextExecutionTick += task.intervalTicks;
                } else {
                    // Remove one-time tasks
                    iterator.remove();
                }
            }
        }
    }

    private static class ScheduledTask {
        private final Runnable runnable; // The task to run
        private final int intervalTicks; // Interval or delay in ticks
        private final boolean cyclic; // Whether the task is cyclic
        private final boolean async; // Whether the task runs asynchronously
        private int nextExecutionTick; // The next tick this task should run

        public ScheduledTask(Runnable runnable, int intervalTicks, boolean cyclic, boolean async) {
            this.runnable = runnable;
            this.intervalTicks = intervalTicks;
            this.cyclic = cyclic;
            this.async = async;
            this.nextExecutionTick = Scheduler.getInstance().currentTick + intervalTicks; // Schedule after the delay
        }
    }
}