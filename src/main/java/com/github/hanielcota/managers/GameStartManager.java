package com.github.hanielcota.managers;


import com.github.hanielcota.events.GameEndEvent;
import com.github.hanielcota.events.GameStartEvent;
import com.github.hanielcota.teams.TeamManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameStartManager {

    private final TeamManager teamManager;
    @Getter
    private boolean gameStarted;

    public GameStartManager(TeamManager teamManager) {
        this.teamManager = teamManager;
        this.gameStarted = false;
    }

    public void playerJoinedGame(Player player) {
        if (!gameStarted) {
            int blueTeamSize = teamManager.getBlueTeamSize();
            int redTeamSize = teamManager.getRedTeamSize();

            if (blueTeamSize == 5 && redTeamSize == 5) {
                startGame(player); // Passa o jogador ao iniciar a partida
            } else {
                int remainingPlayers = 10 - (blueTeamSize + redTeamSize);
                int playersNeeded = 5 - remainingPlayers;

                if (playersNeeded > 0) {
                    broadcastPlayersNeeded(playersNeeded);
                }
            }
        }
    }

    private void broadcastPlayersNeeded(int playersNeeded) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "A partida começará em breve! " + ChatColor.GOLD + "Faltam " + playersNeeded + " jogadores para o início.");
    }

    private void startGame(Player player) {
        gameStarted = true;
        broadcastGameStartMessage();

        GameStartEvent gameStartEvent = new GameStartEvent(teamManager, player);
        Bukkit.getPluginManager().callEvent(gameStartEvent);
    }

    public void forceStartGame(Player player) {
        if (!gameStarted) {
            startGame(player);
        }
    }


    private void broadcastGameStartMessage() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "A partida começou! Boa sorte a todos os jogadores!");
    }

    public void playerLeftGame(Player player) {
        if (gameStarted) {
            checkGameEnd(player);
        }
    }

    private void checkGameEnd(Player player) {
        int blueTeamSize = teamManager.getBlueTeamSize();
        int redTeamSize = teamManager.getRedTeamSize();

        if (blueTeamSize < 5 || redTeamSize < 5) {
            endGame(player);
        }
    }

    private void endGame(Player player) {
        gameStarted = false;
        broadcastGameEndMessage();

        GameEndEvent gameEndEvent = new GameEndEvent(teamManager, player);
        Bukkit.getPluginManager().callEvent(gameEndEvent);
    }

    private void broadcastGameEndMessage() {
        Bukkit.broadcastMessage(ChatColor.RED + "A partida terminou! Obrigado por jogar!");
    }
}
