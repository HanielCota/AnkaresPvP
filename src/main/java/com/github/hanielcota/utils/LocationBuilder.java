package com.github.hanielcota.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocationBuilder extends ConfigUtil {

    private final Map<String, Location> locations;

    public LocationBuilder(Plugin owningPlugin) {
        super(owningPlugin, "locations.yml");
        this.locations = new HashMap<>();
        loadLocations();
    }

    public void loadLocations() {
        ConfigurationSection section = getConfigurationSection("locations");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Location location = parseLocation(section.getConfigurationSection(key));
                if (location != null) {
                    locations.put(key, location);
                }
            }
        }
    }

    private Location parseLocation(ConfigurationSection locationSection) {
        if (locationSection == null) {
            return null;
        }

        World world = getOwningPlugin().getServer().getWorld(locationSection.getString("world"));
        if (world == null) {
            return null;
        }

        double x = locationSection.getDouble("x");
        double y = locationSection.getDouble("y");
        double z = locationSection.getDouble("z");
        float yaw = (float) locationSection.getDouble("yaw");
        float pitch = (float) locationSection.getDouble("pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void saveLocations() {
        ConfigurationSection section = createSection("locations");
        for (Map.Entry<String, Location> entry : locations.entrySet()) {
            String key = entry.getKey();
            Location location = entry.getValue();
            if (location != null) {
                ConfigurationSection locationSection = section.createSection(key);
                locationSection.set("x", location.getX());
                locationSection.set("y", location.getY());
                locationSection.set("z", location.getZ());
                locationSection.set("yaw", location.getYaw());
                locationSection.set("pitch", location.getPitch());
                locationSection.set("world", location.getWorld().getName());
            }
        }
        save();
    }

    public void addLocation(String name, Location location) {
        locations.put(name, location);
        saveLocations();
    }

    public void removeLocation(String name) {
        locations.remove(name);
        saveLocations();
    }
    public Set<String> getWarpNames() {
        return locations.keySet();
    }
    public Location getLocation(String name) {
        return locations.get(name);
    }
}
