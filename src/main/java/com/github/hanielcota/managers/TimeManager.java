package com.github.hanielcota.managers;

import com.github.hanielcota.AnkaresPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeManager {
    private final Plugin plugin;
    private final AnkaresPlugin mainClass;
    @Getter
    private int timeInSeconds;
    private BukkitRunnable countdownTask;

    public TimeManager(Plugin plugin, AnkaresPlugin mainClass) {
        this.plugin = plugin;
        this.mainClass = mainClass;
        this.timeInSeconds = 10 * 60; // 10 minutes in seconds
    }

    public void startCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
        }

        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeInSeconds <= 0) {
                    cancel();
                    endGameTimer();
                    return;
                }

                timeInSeconds--;
            }
        };

        countdownTask.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    public void stopCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
        }
    }

    private void endGameTimer() {
        Bukkit.getOnlinePlayers().forEach(player -> mainClass.getGameStartManager().endGame(player));
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }
}
