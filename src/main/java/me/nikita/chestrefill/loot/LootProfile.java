package me.nikita.chestrefill.loot;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
    public void fillInventory(Inventory inventory) {

        inventory.clear();

        if (lootMap.isEmpty()) return;

        int itemsToAdd = 5 + random.nextInt(6);
        // от 5 до 10 предметов (можешь изменить)

        for (int i = 0; i < itemsToAdd; i++) {

            Material material = getRandomLoot();
            if (material == null) continue;

            ItemStack item = new ItemStack(material, 1);

            inventory.addItem(item);
        }
    }


    public Map<Material, Integer> getLootMap() {
        return lootMap;
    }
}