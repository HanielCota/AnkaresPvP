package com.github.hanielcota.scoreboard;

import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.managers.KillManager;
import com.github.hanielcota.utils.fastboard.FastBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.bukkit.Bukkit.getServer;

public class ScoreboardDesing implements Listener {
    private final AnkaresPlugin plugin;
    private final KillManager killManager;
    private final Map<UUID, FastBoard> boards = new ConcurrentHashMap<>();

    public ScoreboardDesing(AnkaresPlugin plugin, KillManager killManager) {
        this.plugin = plugin;
        this.killManager = killManager;
        taskForUpdateScore();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        FastBoard board = new FastBoard(player);
        boards.put(player.getUniqueId(), board);
        updateBoard(board, player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
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
                "§cVermelho§f: §a" + plugin.getKillManager().getRedKills(),
                "§bAzul§f: §a" + plugin.getKillManager().getBlueKills(),
                "",
                "Tempo: 0:00",
                "",
                "§eankares.com"
        );
    }

    public void taskForUpdateScore() {
        getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (FastBoard board : boards.values()) {
                updateBoard(board, board.getPlayer());
            }
        }, 0, 20);
    }
}
