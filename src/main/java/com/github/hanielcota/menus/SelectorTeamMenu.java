package com.github.hanielcota.menus;

import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.utils.ItemBuilder;
import com.github.hanielcota.utils.inventory.CustomInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.Optional;

public class SelectorTeamMenu extends CustomInventory {

    private final AnkaresPlugin plugin;
    private final Player player;

    public SelectorTeamMenu(AnkaresPlugin plugin, Player player) {
        super(3 * 9, "Selecione o seu time");
        this.plugin = plugin;
        this.player = player;

        int redTeamSize = plugin.getTeamManager().getRedTeamSize();
        int blueTeamSize = plugin.getTeamManager().getBlueTeamSize();

        setItem(12, createTeamItem("Vermelho", ChatColor.RED, redTeamSize, 14));
        setItem(14, createTeamItem("Azul", ChatColor.BLUE, blueTeamSize, 11));
    }

    private ItemStack createTeamItem(String teamName, ChatColor teamColor, int teamSize, int colorNumber) {
        String teamLore = ChatColor.GRAY + "Jogadores no Time " + teamName + ": " + ChatColor.WHITE + teamSize;

        Optional<Team> playerTeam = Optional.ofNullable(plugin.getTeamManager().getPlayerTeam(player));
        ChatColor playerTeamColor = playerTeam.filter(t -> t == plugin.getTeamManager().getRedTeam()).map(t -> ChatColor.RED).orElse(ChatColor.BLUE);

        String joinMessage = (playerTeam.isPresent() && playerTeamColor == teamColor)
                ? "§cVocê já está neste time!"
                : "§aClique para entrar no time " + teamName;

        ItemBuilder item = new ItemBuilder(Material.WOOL)
                .setName(teamColor + teamName)
                .setLore(teamLore, "", joinMessage)
                .setDurability((short) colorNumber);

        return item.build();
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int clickedSlot = event.getRawSlot();

        if (clickedSlot == 12) {
            handleTeamSelection(plugin.getTeamManager().getRedTeam(), "Time Vermelho");
        } else if (clickedSlot == 14) {
            handleTeamSelection(plugin.getTeamManager().getBlueTeam(), "Time Azul");
        }
        plugin.getGameStartManager().playerJoinedGame(player);
    }

    private void handleTeamSelection(Team team, String teamName) {
        Optional<Team> playerTeam = Optional.ofNullable(plugin.getTeamManager().getPlayerTeam(player));

        if (playerTeam.isPresent()) {
            plugin.getTeamManager().removePlayerFromTeams(player);
        }

        try {
            plugin.getTeamManager().addPlayerToTeam(player, team, "§aVocê foi adicionado ao " + teamName + ".");
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10f, 10f);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Ocorreu um erro ao entrar no time " + teamName + ".");
        }

        player.closeInventory();
    }
}
