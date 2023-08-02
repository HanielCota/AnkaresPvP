package com.github.hanielcota.listeners;

import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.events.GameEndEvent;
import com.github.hanielcota.utils.LocationBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameEndListener implements Listener {

    private final AnkaresPlugin plugin;

    public GameEndListener(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        teleportPlayerToTeamLocation(event.getPlayer(), "spawn");
    }

    private void teleportPlayerToTeamLocation(Player player, String locationName) {
        LocationBuilder locationBuilder = new LocationBuilder(plugin);
        Location location = locationBuilder.getLocation(locationName);

        if (location == null) {
            player.sendMessage("§cLocalização '" + locationName + "' não encontrada.");
            return;
        }

        player.teleport(location);
    }
}
