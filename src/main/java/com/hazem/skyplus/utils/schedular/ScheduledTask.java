package com.hazem.skyplus.utils.schedular;

public class ScheduledTask {
    protected final Runnable runnable;
    protected final int intervalTicks;
    protected final boolean cyclic;
    protected final boolean async;
    protected int nextExecutionTick;
    protected boolean active;

    public ScheduledTask(Runnable runnable, int intervalTicks, boolean cyclic, boolean async, int nextExecutionTick, boolean active) {
        this.runnable = runnable;
        this.intervalTicks = intervalTicks;
        this.cyclic = cyclic;
        this.async = async;
        this.nextExecutionTick = nextExecutionTick;
        this.active = active;
    }

    public void next(int currentTick) {
        nextExecutionTick = currentTick + intervalTicks;
    }

    public void updateNextExecutionTick(int newStartTick) {
        nextExecutionTick = newStartTick;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }
}