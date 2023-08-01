package com.github.hanielcota.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.utils.ClickMessage;
import com.github.hanielcota.utils.LocationBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@CommandAlias("warp")
public class WarpCommand extends BaseCommand {

    private final AnkaresPlugin plugin;
    private final Cache<Player, String> confirmations;


    public WarpCommand(AnkaresPlugin plugin) {
        this.plugin = plugin;
        this.confirmations = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build();
    }

    @Default
    public void onWarpCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cPor favor, especifique o nome da localização.");
            return;
        }

        String locationName = args[0];
        LocationBuilder locationBuilder = new LocationBuilder(plugin);
        Location location = locationBuilder.getLocation(locationName);

        if (location == null) {
            player.sendMessage("§cLocalização '" + locationName + "' não encontrada.");
            return;
        }

        player.teleport(location);
        player.sendMessage("§eTeleportado para a localização '" + locationName + "'.");
    }

    @Subcommand("set")
    public void onSetWarpCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cPor favor, especifique um nome para a localização.");
            return;
        }

        Location location = player.getLocation();
        LocationBuilder locationBuilder = new LocationBuilder(plugin);
        String locationName = args[0];

        if (locationBuilder.getLocation(locationName) != null) {
            player.sendMessage("§cJá existe uma localização com o nome '" + locationName + "'. Por favor, escolha um nome diferente.");
            return;
        }

        try {
            locationBuilder.addLocation(locationName, location);
            player.sendMessage("§aLocalização '" + locationName + "' configurada na sua posição atual.");
        } catch (Exception e) {
            player.sendMessage("§cFalha ao configurar a localização '" + locationName + "'. Por favor, tente novamente mais tarde.");
        }
    }

    @Subcommand("list")
    public void onListWarpsCommand(Player player) {
        LocationBuilder locationBuilder = new LocationBuilder(plugin);
        Set<String> warpNames = locationBuilder.getWarpNames();

        if (warpNames.isEmpty()) {
            player.sendMessage("§cNão há nenhuma warp configurada.");
            return;
        }

        player.sendMessage("\n§eWarps disponíveis:");

        for (String warpName : warpNames) {
            ClickMessage clickMessage = new ClickMessage("§f- " + warpName);
            clickMessage.click(ClickEvent.Action.RUN_COMMAND, "/warp " + warpName);
            clickMessage.tooltip("§eClique para ir até a warp '" + warpName + "'.");
            clickMessage.send(player);
        }
        player.sendMessage("");
        player.sendMessage("§eClique sobre o nome da warp para teleportar-se.");
    }

    @Subcommand("delete")
    public void onDeleteWarpCommand(Player player) {
        LocationBuilder locationBuilder = new LocationBuilder(plugin);
        Set<String> warpNames = locationBuilder.getWarpNames();

        if (warpNames.isEmpty()) {
            player.sendMessage("§cNão há nenhuma warp configurada para deletar.");
            return;
        }

        player.sendMessage("\n§6Warps disponíveis para deletar:");

        for (String warpName : warpNames) {
            ClickMessage clickMessage = new ClickMessage("§e- §f" + warpName);
            clickMessage.click(ClickEvent.Action.RUN_COMMAND, "/warp deleteconfirm " + warpName);
            clickMessage.tooltip("§eClique para confirmar a exclusão da warp §f'" + warpName + "'§e.");
            clickMessage.send(player);
        }

        player.sendMessage("");
        player.sendMessage("§eClique sobre o nome da warp para confirmar a exclusão.");
    }

    @Subcommand("deleteconfirm")
    public void onDeleteConfirmCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cPor favor, especifique o nome da warp que deseja deletar.");
            return;
        }

        String warpName = args[0];
        LocationBuilder locationBuilder = new LocationBuilder(plugin);

        if (locationBuilder.getLocation(warpName) == null) {
            player.sendMessage("§cLocalização '" + warpName + "' não encontrada.");
            return;
        }

        String previousConfirmation = confirmations.getIfPresent(player);
        if (previousConfirmation == null || !previousConfirmation.equals(warpName)) {
            player.sendMessage(new String[]{
                    "",
                    "§eTem certeza que deseja deletar a warp §f'" + warpName + "'§e?",
                    "§eClique novamente no nome da warp para confirmar a exclusão.",
                    ""
            });
            confirmations.put(player, warpName);
            return;
        }

        locationBuilder.removeLocation(warpName);
        player.sendMessage("§aWarp §f'" + warpName + "'§a deletada com sucesso.");
        confirmations.invalidate(player);

    }
}

