package com.github.hanielcota.managers;

import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.teams.TeamManager;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KillManager {

    private final AnkaresPlugin plugin;
    private final Map<Player, Integer> playerKills;
    @Getter
    private int blueKills;
    @Getter
    private int redKills;

    public KillManager(AnkaresPlugin plugin) {
        this.plugin = plugin;
        this.playerKills = new HashMap<>();
        this.blueKills = 0;
        this.redKills = 0;
    }

    public void addKill(Player player) {
        int currentKills = playerKills.getOrDefault(player, 0);
        playerKills.put(player, currentKills + 1);

        TeamManager teamManager = plugin.getTeamManager();
        if (teamManager == null) {
            return;
        }

        if (teamManager.getPlayerTeam(player) == teamManager.getBlueTeam()) {
            blueKills++;
        } else if (teamManager.getPlayerTeam(player) == teamManager.getRedTeam()) {
            redKills++;
        }
    }

    public int getKills(Player player) {
        return playerKills.getOrDefault(player, 0);
    }

    public void resetKills(Player player) {
        playerKills.put(player, 0);
    }

    public void resetAllKills() {
        playerKills.clear();
        blueKills = 0;
        redKills = 0;
    }
}
