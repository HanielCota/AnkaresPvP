package com.github.hanielcota.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for setting custom headers and footers in the Tablist.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TabUtil {

    private static final String VERSION_NUMBER = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final String NMS_PACKAGE = "net.minecraft.server";
    private static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final String NMS_VERSIONED_CLASS_PATH = NMS_PACKAGE + "." + NMS_VERSION;

    public static void setHeaderFooter(Player player, String header, String footer) {
        if (player == null) {
            return;
        }
        try {
            Class<?> iChatBaseComponent = Class.forName("net.minecraft.server." + VERSION_NUMBER + ".IChatBaseComponent");
            Class<?> chatSerializer = iChatBaseComponent.getDeclaredClasses()[0];
            Class<?> packetOutHeaderFooter = Class.forName("net.minecraft.server." + VERSION_NUMBER + ".PacketPlayOutPlayerListHeaderFooter");
            Method a = chatSerializer.getMethod("a", String.class);

            header = ChatColor.translateAlternateColorCodes('&', header);
            footer = ChatColor.translateAlternateColorCodes('&', footer);

            Object chatBaseCompHeader = a.invoke(null, header.startsWith("{") ? header : "{\"text\":\"" + header + "\"}");
            Object chatBaseCompFooter = a.invoke(null, footer.startsWith("{") ? footer : "{\"text\":\"" + footer + "\"}");
            Object headerFooterPacket = packetOutHeaderFooter.getDeclaredConstructor(iChatBaseComponent).newInstance(chatBaseCompHeader);

            setDeclaredField(headerFooterPacket, "b", chatBaseCompFooter);
            sendPacket(player, headerFooterPacket);
        } catch (Exception e) {
            // Proper error handling with logging
            Logger.getLogger(TabUtil.class.getName()).log(Level.SEVERE, "Error while setting header and footer", e);
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            Logger.getLogger(TabUtil.class.getName()).log(Level.SEVERE, "Error while sending packet", e);
        }
    }

    private static void setDeclaredField(Object o, String name, Object value) {
        try {
            java.lang.reflect.Field f = o.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(o, value);
            f.setAccessible(false); // Release the resource after use
        } catch (Exception e) {
            // Proper error handling with logging
            Logger.getLogger(TabUtil.class.getName()).log(Level.SEVERE, "Error while setting declared field", e);
        }
    }

    private static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName(NMS_VERSIONED_CLASS_PATH + "." + name);
    }
}
