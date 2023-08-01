package com.github.hanielcota.listeners;

import com.github.hanielcota.AnkaresPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final AnkaresPlugin plugin;

    public PlayerJoinListener(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getTeamManager().isPlayerInTeam(player)) {
            plugin.getTeamManager().addPlayerToRedTeam(player);
        }

        event.setJoinMessage(null);
    }
}
