package com.github.hanielcota.listeners;

import com.github.hanielcota.AnkaresPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final AnkaresPlugin plugin;

    public PlayerQuitListener(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.getTeamManager().removePlayerFromTeams(player);
        plugin.getGameStartManager().playerLeftGame(player);

        event.setQuitMessage(null);
    }
}
