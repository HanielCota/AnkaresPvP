package com.github.hanielcota.events;


import com.github.hanielcota.teams.TeamManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class GameEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final TeamManager teamManager;
    private final Player player;

    public GameEndEvent(TeamManager teamManager, Player player) {
        this.teamManager = teamManager;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
