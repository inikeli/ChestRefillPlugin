package me.nikita.chestrefill;

import me.nikita.chestrefill.command.ChestRefillCommand;
import me.nikita.chestrefill.region.RegionManager;
import me.nikita.chestrefill.loot.LootProfileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChestRefillPlugin extends JavaPlugin {

    private RegionManager regionManager;
    private LootProfileManager lootProfileManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();


        lootProfileManager = new LootProfileManager(this);


        regionManager = new RegionManager(this, lootProfileManager);

        // 3️⃣ Регистрируем команду
        getCommand("chestrefill")
                .setExecutor(new ChestRefillCommand(regionManager, lootProfileManager));

        getLogger().info("ChestRefill enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChestRefill disabled!");
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public LootProfileManager getLootProfileManager() {
        return lootProfileManager;
    }
}