package no.hyp.stacksize;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Stacksize extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Create a configuration file in the plugin's directory if it does not exist.
        saveDefaultConfig();
        // Read configuration and modify the server's item stack sizes.
        applyConfiguration();
        // Register the inventory updater.
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    /**
     * Read the stack sizes from the configuration and apply them to materials on the server.
     */
    private void applyConfiguration() {
        Map<Material, Integer> stackSizes = new HashMap<>();
        // Parse the entries in the maxStackSize list and update the corresponding item max stack sizes.
        for (String mMaterial : getConfig().getConfigurationSection("stackSizes").getKeys(false)) {
            Material material = Material.matchMaterial(mMaterial);
            if (material == null) {
                this.getLogger().warning(String.format("Unable to match string to material: \"%s\". Skipping.", mMaterial));
                continue;
            }
            int maxStackSize;
            try {
                maxStackSize = getConfig().getInt("stackSizes." + mMaterial);
            } catch (NumberFormatException e) {
                this.getLogger().warning(String.format("Unable to parse integer: \"%s\". Skipping.", getConfig().getString("stackSizes." + mMaterial)));
                continue;
            }
            stackSizes.put(material, maxStackSize);
        }
        for (Map.Entry<Material, Integer> entry : stackSizes.entrySet()) {
            modifyStackSize(entry.getKey(), entry.getValue());
        }
    }

    public int getStackSize(Material material) {
        return material.getMaxStackSize();
    }

    public boolean updateConfiguration(Material material, int stackSize) {
        this.getConfig().set("stackSizes." + material.name(), stackSize);
        this.saveConfig();
        return true;
    }

    /**
     * Modify the maximum stack size of an item on the server.
     *
     * @param material
     * @param size
     * @return
     */
    public boolean modifyStackSize(Material material, int size) {
        try {
            // Convert a Material into its Item.
            Class<?> magicClass = Class.forName("org.bukkit.craftbukkit." + parseVersion() + ".util.CraftMagicNumbers");
            Method method = magicClass.getDeclaredMethod("getItem", Material.class);
            Object item = method.invoke(null, material);
            // Get the maxItemStack field in Item and change it.
            Class<?> itemClass = Class.forName("net.minecraft.server." + parseVersion() + ".Item");
            Field field = itemClass.getDeclaredField("maxStackSize");
            field.setAccessible(true);
            field.setInt(item, size);
            {
                Field mf = Material.class.getDeclaredField("maxStack");
                mf.setAccessible(true);
                mf.setInt(material, size);
            }
            this.getLogger().info(String.format("Set maximum stack size of %-20s to %d.", material.name(), size));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.getLogger().warning("Reflection error.");
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (command.getName().equalsIgnoreCase("stacksize")) {
            if (arguments.length >= 1) {
                String subCommand = arguments[0];
                switch (subCommand) {
                    case "set":
                        if (arguments.length == 3) {
                            String materialName = arguments[1];
                            Material material = Material.matchMaterial(materialName);
                            if (material == null) {
                                sender.sendMessage(ChatColor.RED + "There is no material of that name.");
                                return true;
                            }
                            int maxStackSize;
                            try {
                                maxStackSize = Integer.parseInt(arguments[2]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(String.format(ChatColor.RED + "Unable to parse integer argument %s.", arguments[2]));
                                return true;
                            }
                            modifyStackSize(material, maxStackSize);
                            updateConfiguration(material, maxStackSize);
                            sender.sendMessage(String.format(ChatColor.YELLOW + "Changed maximum stack size of " + ChatColor.RESET + "%s" + ChatColor.YELLOW + " to " + ChatColor.RESET + "%d" + ChatColor.YELLOW + ".", material, material.getMaxStackSize()));
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "/stacksize set <material> <stacksize>");
                            return true;
                        }
                    case "info":
                        if (arguments.length == 2) {
                            String materialName = arguments[1];
                            Material material = Material.matchMaterial(materialName);
                            if (material != null) {
                                sender.sendMessage(String.format(ChatColor.YELLOW + "Material: " + ChatColor.RESET + "%s" + ChatColor.YELLOW + ", maxStackSize: " + ChatColor.RESET + "%d", material, material.getMaxStackSize()));
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.RED + "There is no material of that name.");
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "/stacksize info <material>");
                            return true;
                        }
                    case "inspect":
                        if (arguments.length == 1) {
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                ItemStack item = player.getInventory().getItemInMainHand();
                                int max = item.getMaxStackSize();
                                Material material = item.getType();
                                sender.sendMessage(String.format(ChatColor.YELLOW + "Material: " + ChatColor.RESET + "%s" + ChatColor.YELLOW + ", maxStackSize: " + ChatColor.RESET + "%d", material, max));
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.RED + "You must be a player to inspect the item in your hand.");
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "/stacksize inspect <material>");
                            return true;
                        }
                    default:
                        sender.sendMessage(ChatColor.RED + "/stacksize <set | info | inspect>");
                        return true;
                }
            }
            sender.sendMessage(ChatColor.RED + "/stacksize <set | info | inspect>");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {
        if (command.getName().equalsIgnoreCase("stacksize")) {
            if (arguments.length == 0) {
                return new ArrayList<>();
            } else if (arguments.length == 1) {
                List<String> subCommands = new ArrayList<>();
                subCommands.add("set");
                subCommands.add("info");
                subCommands.add("inspect");
                String subCommand = arguments[0];
                return subCommands.stream().filter(x -> x.startsWith(subCommand.toLowerCase())).collect(Collectors.toList());
            } else if (arguments.length == 2) {
                String subCommand = arguments[0];
                if (subCommand.equalsIgnoreCase("set") || subCommand.equalsIgnoreCase("info")) {
                    String mMaterial = arguments[1];
                    return Arrays.stream(Material.values()).filter(x -> x.name().startsWith(mMaterial.toUpperCase())).map(Material::toString).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else if (arguments.length == 3) {
                String subCommand = arguments[0];
                if (subCommand.equalsIgnoreCase("set")) {
                    String number = arguments[2];
                    List<String> stackSizes = new ArrayList<>();
                    stackSizes.add("64");
                    stackSizes.add("32");
                    stackSizes.add("16");
                    stackSizes.add("4");
                    stackSizes.add("1");
                    return stackSizes.stream().filter(x -> x.startsWith(number)).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            }
        } else {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    /**
     * Parse the version string that is part of the name of some internal server objects.
     */
    private String parseVersion() {
        //return this.getServer().getClass().getPackage().getName().split(".")[3];
        return "v1_14_R1";
    }

    /**
     * This listener will update the player's inventory the tick after an inventory click. This is required
     * since the client predicts how the inventory will look afterwards. When the server has modified stack
     * sizes the prediction might be wrong.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            // The creative inventory works differently to the survival inventory and will not work
            // properly when updating the inventory the tick after a click.
            if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                Bukkit.getScheduler().runTask(this, player::updateInventory);
            }
        }
    }

}
