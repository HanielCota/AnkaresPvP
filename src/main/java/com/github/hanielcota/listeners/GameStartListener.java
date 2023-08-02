package com.github.hanielcota.listeners;

import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.events.GameStartEvent;
import com.github.hanielcota.teams.TeamManager;
import com.github.hanielcota.utils.LocationBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

public class GameStartListener implements Listener {

    private final AnkaresPlugin plugin;

    public GameStartListener(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Player starter = event.getPlayer();

        TeamManager teamManager = event.getTeamManager();

        Team playerTeam = teamManager.getPlayerTeam(starter);
        if (playerTeam == null) {
            starter.sendMessage(ChatColor.RED + "Você não está em nenhum time.");
            return;
        }

        String playerTeamName = playerTeam.getName();

        if (playerTeamName.equalsIgnoreCase("blue")) {
            teleportPlayerToTeamLocation(starter, "azul");
            return;
        }

        if (playerTeamName.equalsIgnoreCase("red")) {
            teleportPlayerToTeamLocation(starter, "vermelho");
        }
        starter.getInventory().clear();

        plugin.getTimeManager().startCountdown();
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
