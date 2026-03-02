package me.nikita.chestrefill.loot;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LootProfileManager {

    private final JavaPlugin plugin;
    private final Map<String, LootProfile> profiles = new HashMap<>();
    private final File file;

    public LootProfileManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "loot-profiles.yml");
        load();
    }

    public LootProfile getProfile(String name) {
        return profiles.get(name.toLowerCase());
    }

    public Map<String, LootProfile> getProfiles() {
        return profiles;
    }

    public void load() {
        try {
            if (!file.exists()) {
                plugin.saveResource("loot-profiles.yml", false);
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            for (String profileName : config.getKeys(false)) {
                LootProfile profile = new LootProfile(profileName);

                for (String materialName : config.getConfigurationSection(profileName).getKeys(false)) {
                    Material mat = Material.getMaterial(materialName);
                    int chance = config.getInt(profileName + "." + materialName);

                    if (mat != null) {
                        profile.addLoot(mat, chance);
                    }
                }

                profiles.put(profileName.toLowerCase(), profile);
            }

        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при загрузке loot-профилей: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (LootProfile profile : profiles.values()) {
            for (Map.Entry<Material, Integer> entry : profile.getLootMap().entrySet()) {
                config.set(profile.getName() + "." + entry.getKey().name(), entry.getValue());
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить loot-профили: " + e.getMessage());
        }
    }
}