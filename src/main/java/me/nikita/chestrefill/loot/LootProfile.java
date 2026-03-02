package me.nikita.chestrefill.loot;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LootProfile {

    private final String name;
    private final Map<Material, Integer> lootMap = new HashMap<>();
    private final Random random = new Random();

    public LootProfile(String name) {
        this.name = name;
    }

    public void addLoot(Material material, int chance) {
        lootMap.put(material, chance);
    }

    public String getName() { return name; }

    // Получить случайный предмет по шансам
    public Material getRandomLoot() {
        int total = lootMap.values().stream().mapToInt(Integer::intValue).sum();
        int roll = random.nextInt(total) + 1;

        int current = 0;
        for (Map.Entry<Material, Integer> entry : lootMap.entrySet()) {
            current += entry.getValue();
            if (roll <= current) {
                return entry.getKey();
            }
        }

        return null; // на случай ошибки
    }

    public Map<Material, Integer> getLootMap() {
        return lootMap;
    }
}