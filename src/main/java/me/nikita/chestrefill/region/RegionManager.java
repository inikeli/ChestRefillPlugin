package me.nikita.chestrefill.region;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class RegionManager {

    private final JavaPlugin plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void addRegion(Region region) {
        regions.put(region.getName().toLowerCase(), region);
        save(region);
    }

    public Region getRegion(String name) {
        return regions.get(name.toLowerCase());
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    private void save(Region region) {
        String path = "regions." + region.getName();

        var config = plugin.getConfig();

        config.set(path + ".world", region.getWorldName());
        config.set(path + ".profile", region.getProfile());

        config.set(path + ".minX", region.getMinX());
        config.set(path + ".minY", region.getMinY());
        config.set(path + ".minZ", region.getMinZ());
        config.set(path + ".maxX", region.getMaxX());
        config.set(path + ".maxY", region.getMaxY());
        config.set(path + ".maxZ", region.getMaxZ());

        plugin.saveConfig();
    }

    private void load() {
        var config = plugin.getConfig();

        if (!config.contains("regions")) return;

        ConfigurationSection section = config.getConfigurationSection("regions");

        for (String name : section.getKeys(false)) {

            String worldName = section.getString(name + ".world");
            String profile = section.getString(name + ".profile");

            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;

            Region region = new Region(
                    name,
                    profile,
                    world,
                    section.getInt(name + ".minX"),
                    section.getInt(name + ".minY"),
                    section.getInt(name + ".minZ"),
                    section.getInt(name + ".maxX"),
                    section.getInt(name + ".maxY"),
                    section.getInt(name + ".maxZ")
            );

            regions.put(name.toLowerCase(), region);
        }
    }
}