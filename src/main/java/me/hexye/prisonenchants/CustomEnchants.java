package me.hexye.prisonenchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomEnchants {

    public static final Enchantment DOUBLE_BREAK = new EnchantmentWrapper("Double Break", 5);
    public static final Enchantment ZONE_BREAK = new EnchantmentWrapper("Zone Break", 5);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(DOUBLE_BREAK);
        if (!registered) {
            registerEnchantment(DOUBLE_BREAK);
        }
        registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(ZONE_BREAK);
        if (!registered) {
            registerEnchantment(ZONE_BREAK);
        }
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch(Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if (registered) {
            Bukkit.getLogger().info(ChatColor.GREEN + "Registered enchantment " + enchantment.getName());
        } else {
            Bukkit.getLogger().info(ChatColor.RED + "Failed to register enchantment " + enchantment.getName());
        }
    }
}
