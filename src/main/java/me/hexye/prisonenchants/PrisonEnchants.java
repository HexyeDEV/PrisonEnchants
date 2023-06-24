package me.hexye.prisonenchants;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

import static net.md_5.bungee.api.ChatColor.translateAlternateColorCodes;

public class PrisonEnchants extends JavaPlugin {
    @Override
    public void onEnable() {
        CustomEnchants.register();
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(), "messages.yml");
        if (!file.exists()) {
            getLogger().info("messages.yml not found, creating!");
            saveResource("messages.yml", false);
        }
        this.getServer().getPluginManager().registerEvents(new DoubleBreakEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new ZoneBreakEvent(), this);
        Bukkit.getLogger().info(ChatColor.GREEN + "PowerfulEnchants has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "PowerfulEnchants has been disabled!");
    }

    public String getMessage(String message) {
        File file = new File(getDataFolder(), "messages.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return translateAlternateColorCodes('&', config.getString(message));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label != "pe" || label != "prisonenchants") {
            return true;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            String help_1 = getMessage("help-1");
            String help_2 = getMessage("help-2");
            String help_3 = getMessage("help-3");
            sender.sendMessage(help_1);
            sender.sendMessage(help_2);
            sender.sendMessage(help_3);
        }
        if (args[0].equalsIgnoreCase("zonebreak")) {
            String lvl = args[1];
            Integer level = Integer.parseInt(lvl);
            Player player = (Player) sender;
            if (!player.hasPermission("powerfulenchants.zonebreak")) {
                String no_permission = getMessage("no-permission");
                sender.sendMessage(no_permission);
                return true;
            }
            if (!(sender instanceof Player)) {
                String not_player = getMessage("not-player");
                sender.sendMessage(not_player);
                return true;
            }
            Object material;
            ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
            item.addUnsafeEnchantment(CustomEnchants.ZONE_BREAK, level);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GRAY + "Zone Break " + lvl);
            meta.setLore(lore);
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
            String zonebreak_given = getMessage("zonebreak-given");
            String formatted = String.format(zonebreak_given, lvl);
            sender.sendMessage(formatted);
            return true;
        }
        if (args[0].equalsIgnoreCase("doublebreak")) {
            String lvl = args[1];
            Integer level = Integer.parseInt(lvl);
            Player player = (Player) sender;
            if (!player.hasPermission("powerfulenchants.doublebreak")) {
                String no_permission = getMessage("no-permission");
                sender.sendMessage(no_permission);
                return true;
            }
            if (!(sender instanceof Player)) {
                String not_player = getMessage("not-player");
                sender.sendMessage(not_player);
                return true;
            }
            ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
            item.addUnsafeEnchantment(CustomEnchants.DOUBLE_BREAK, level);

            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GRAY + "Double Break " + lvl);
            meta.setLore(lore);
            item.setItemMeta(meta);

            player.getInventory().addItem(item);
            String doublebreak_given = getMessage("doublebreak-given");
            String formatted = String.format(doublebreak_given, lvl);
            sender.sendMessage(formatted);
            return true;
        }
        return true;
    }
}