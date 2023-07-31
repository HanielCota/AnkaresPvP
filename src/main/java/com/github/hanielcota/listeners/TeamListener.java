package com.github.hanielcota.listeners;

import com.github.hanielcota.AnkaresPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TeamListener implements Listener {
    private final AnkaresPlugin plugin;

    public TeamListener(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getTeamManager().isPlayerInTeam(player)) {
            plugin.getTeamManager().addPlayerToRedTeam(player);
        }
    }
}
