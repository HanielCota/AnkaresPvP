package com.github.hanielcota.teams;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@Getter
public class TeamManager {

    private final Team blueTeam;
    private final Team redTeam;

    public TeamManager(Scoreboard scoreboard) {
        this.blueTeam = getOrCreateTeam(scoreboard, "blue", ChatColor.BLUE + "Time Azul", ChatColor.BLUE);
        this.redTeam = getOrCreateTeam(scoreboard, "red", ChatColor.RED + "Time Vermelho", ChatColor.RED);
    }

    private Team getOrCreateTeam(Scoreboard scoreboard, String name, String displayName, ChatColor color) {
        Team team = scoreboard.getTeam(name);
        if (team == null) {
            team = scoreboard.registerNewTeam(name);
        }
        team.setDisplayName(displayName);
        team.setPrefix(color.toString());
        return team;
    }

    public void addPlayerToBlueTeam(Player player) {
        addPlayerToTeam(player, blueTeam, ChatColor.BLUE + "Você foi adicionado ao Time Azul.");
    }

    public void addPlayerToRedTeam(Player player) {
        addPlayerToTeam(player, redTeam, ChatColor.RED + "Você foi adicionado ao Time Vermelho.");
    }

    public void addPlayerToTeam(Player player, Team team, String message) {
        team.addEntry(player.getName());
        player.sendMessage(message);
    }

    public void removePlayerFromTeams(Player player) {
        blueTeam.removeEntry(player.getName());
        redTeam.removeEntry(player.getName());
    }

    public boolean isPlayerInTeam(Player player) {
        return blueTeam.hasEntry(player.getName()) || redTeam.hasEntry(player.getName());
    }

    public Team getPlayerTeam(Player player) {
        if (blueTeam.hasEntry(player.getName())) {
            return blueTeam;
        } else if (redTeam.hasEntry(player.getName())) {
            return redTeam;
        }
        return null;
    }

    public int getBlueTeamSize() {
        return blueTeam.getSize();
    }

    public int getRedTeamSize() {
        return redTeam.getSize();
    }

    public void unregisterTeams() {
        blueTeam.unregister();
        redTeam.unregister();
    }

    public void clearTeamsOnStartup() {
        clearTeam(blueTeam);
        clearTeam(redTeam);
    }

    private void clearTeam(Team team) {
        if (team != null) {
            for (String entry : team.getEntries()) {
                team.removeEntry(entry);
            }
        }
    }
}

