package com.github.hanielcota.listeners;

import com.github.hanielcota.teams.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final TeamManager teamManager;

    public PlayerQuitListener(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Remover o jogador dos times quando ele sair do servidor
        teamManager.removePlayerFromTeams(player);

        // Definir a mensagem de saída como nula para evitar que a mensagem padrão seja enviada
        event.setQuitMessage(null);
    }
}
