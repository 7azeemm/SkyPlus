package com.hazem.skyplus.utils.schedular;

import com.mojang.brigadier.Command;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * A utility class for scheduling tasks.
 * Allows for synchronous, asynchronous, and cyclic task scheduling.
 */
public class Scheduler {
    private static final Scheduler INSTANCE = new Scheduler();
    private final Object2ObjectOpenHashMap<Integer, ScheduledTask> tasks = new Object2ObjectOpenHashMap<>();
    private int currentTick = 0;
    private int taskIdCounter = 0; // Unique ID counter

    public static Scheduler getInstance() {
        return INSTANCE;
    }

    public int getCurrentTick() {
        return currentTick;
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
     * Creates a lazy task that is initially inactive.
     * Can be activated later using `activateTask(id)`.
     */
    public int scheduleLazy(Runnable task, int delay) {
        return addTask(task, delay, 0, true, false, false);
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
        addTask(task, delay, intervalTicks, cyclic, async, true);
    }

    private int addTask(Runnable task, int delay, int intervalTicks, boolean cyclic, boolean async, boolean active) {
        int taskId = taskIdCounter++;
        ScheduledTask scheduledTask = new ScheduledTask(task, intervalTicks, cyclic, async, currentTick + delay, active);
        tasks.put(taskId, scheduledTask);
        return taskId;
    }

    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    public void activateTask(int taskId) {
        ScheduledTask task = tasks.get(taskId);
        if (task != null) {
            task.setActive(true);
        }
    }

    public void deactivateTask(int taskId) {
        ScheduledTask task = tasks.get(taskId);
        if (task != null) {
            task.setActive(false);
        }
    }

    /**
     * Reschedules an existing task without removing and re-adding it.
     */
    public void rescheduleTask(int taskId, int delay) {
        ScheduledTask task = tasks.get(taskId);
        if (task != null) {
            task.updateNextExecutionTick(currentTick + delay);
            task.setActive(true);
        }
    }

    public void tick(MinecraftClient client) {
        currentTick++; // Increment the current tick counter

        Iterator<Map.Entry<Integer, ScheduledTask>> iterator = tasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, ScheduledTask> entry = iterator.next();
            ScheduledTask task = entry.getValue();

            if (!task.isActive()) continue;

            // Check if the task is ready to run
            if (currentTick >= task.nextExecutionTick) {
                if (task.async) {
                    // Runs a task asynchronously.
                    client.executeAsync(future -> CompletableFuture.runAsync(task.runnable));
                } else {
                    // Runs the task synchronously.
                    client.execute(task.runnable);
                }

                if (task.cyclic) {
                    // Reschedule for the next cycle
                    task.next(currentTick);
                } else {
                    iterator.remove();
                }
            }
        }
    }
}