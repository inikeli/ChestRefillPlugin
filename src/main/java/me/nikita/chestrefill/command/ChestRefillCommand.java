package me.nikita.chestrefill.command;

import me.nikita.chestrefill.loot.LootProfileManager;
import me.nikita.chestrefill.region.Region;
import me.nikita.chestrefill.region.RegionManager;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestRefillCommand implements CommandExecutor {

    private final RegionManager regionManager;
    private final LootProfileManager lootProfileManager;

    public ChestRefillCommand(RegionManager regionManager, LootProfileManager lootProfileManager) {
        this.regionManager = regionManager;
        this.lootProfileManager = lootProfileManager;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игрок может использовать команду.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cИспользование: /chestrefill <set|refill>");
            return true;
        }

        // =========================
        // SET
        // =========================
        if (args[0].equalsIgnoreCase("set")) {

            if (args.length != 9) {
                player.sendMessage("§cИспользование: /chestrefill set <name> <profile> <x1> <y1> <z1> <x2> <y2> <z2>");
                return true;
            }

            try {
                String name = args[1];
                String profile = args[2];

                if (lootProfileManager.getProfile(profile.toLowerCase()) == null) {
                    player.sendMessage("§cПрофиль §f" + profile + " §cне найден!");
                    return true;
                }

                int x1 = Integer.parseInt(args[3]);
                int y1 = Integer.parseInt(args[4]);
                int z1 = Integer.parseInt(args[5]);
                int x2 = Integer.parseInt(args[6]);
                int y2 = Integer.parseInt(args[7]);
                int z2 = Integer.parseInt(args[8]);

                World world = player.getWorld();

                Region region = new Region(name, profile, world, x1, y1, z1, x2, y2, z2);
                regionManager.addRegion(region);

                player.sendMessage("§aРегион §f" + name + " §aсохранен.");

            } catch (NumberFormatException e) {
                player.sendMessage("§cКоординаты должны быть числами.");
            }

            return true;
        }

        // =========================
        // REFILL
        // =========================
        if (args[0].equalsIgnoreCase("refill")) {

            if (args.length != 2) {
                player.sendMessage("§cИспользование: /chestrefill refill <name>");
                return true;
            }

            String regionName = args[1];

            Region region = regionManager.getRegion(regionName);
            if (region == null) {
                player.sendMessage("§cРегион не найден.");
                return true;
            }

            regionManager.refillRegion(region);
            player.sendMessage("§aРегион §f" + regionName + " §aперезаполнен.");

            return true;
        }

        player.sendMessage("§cНеизвестная подкоманда.");
        return true;
    }
}