package com.github.hanielcota.scoreboard;

import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.managers.KillManager;
import com.github.hanielcota.utils.TimeUtils;
import com.github.hanielcota.utils.fastboard.FastBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class ScoreboardDesign implements Listener {
    private final AnkaresPlugin plugin;
    private final KillManager killManager;
    private final Map<UUID, FastBoard> boards = new ConcurrentHashMap<>();

    public ScoreboardDesign(AnkaresPlugin plugin, KillManager killManager) {
        this.plugin = plugin;
        this.killManager = killManager;
        taskForUpdateScore();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FastBoard board = new FastBoard(player);
        boards.put(player.getUniqueId(), board);
        updateBoard(board, player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    private void updateBoard(FastBoard board, Player player) {
        board.updateTitle("§e§lANKARES");
        board.updateLines(
                "",
                "§fAbates: §a" + killManager.getKills(player),
                "§fHabilidade: §aNenhuma",
                "",
                "§cVermelho: §a" + plugin.getKillManager().getRedKills(),
                "§bAzul: §a" + plugin.getKillManager().getBlueKills(),
                "",
                "Tempo: §a" + TimeUtils.formatTime(plugin.getTimeManager().getTimeInSeconds()),
                "",
                "§eankares.com"
        );
    }


    public void taskForUpdateScore() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (FastBoard board : boards.values()) {
                    updateBoard(board, board.getPlayer());
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }
}
