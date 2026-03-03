package me.nikita.chestrefill.region;
import me.nikita.chestrefill.loot.LootProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import java.util.HashMap;
import java.util.Map;

public class RegionManager {

    private final JavaPlugin plugin;
    private final Map<String, Region> regions = new HashMap<>();

    public RegionManager(JavaPlugin plugin, LootProfileManager lootProfileManager) {
        this.plugin = plugin;
        this.lootProfileManager = lootProfileManager;
        load();
    }


    public void addRegion(Region region) {
        regions.put(region.getName().toLowerCase(), region);
        scanChests(region);
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
    private final LootProfileManager lootProfileManager;
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
            scanChests(region);
            regions.put(name.toLowerCase(), region);
        }
    }
    public void scanChests(Region region) {

        region.clearChests();

        int minX = region.getMinX();
        int maxX = region.getMaxX();
        int minY = region.getMinY();
        int maxY = region.getMaxY();
        int minZ = region.getMinZ();
        int maxZ = region.getMaxZ();

        var world = region.getWorld();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {

                    var block = world.getBlockAt(x, y, z);

                    if (block.getState() instanceof Chest chest) {
                        region.addChest(block.getLocation());
                    }
                }
            }
        }

        plugin.getLogger().info("Найдено сундуков: " + region.getChests().size());
    }
    public void refillRegion(Region region) {

        if (region == null) return;

        var profile = lootProfileManager.getProfile(region.getProfile());
        if (profile == null) return;

        for (Location loc : region.getChests()) {

            if (!(loc.getBlock().getState() instanceof Chest chest))
                continue;

            var inventory = chest.getInventory();
            inventory.clear();
            profile.fillInventory(inventory);
        }
    }

}