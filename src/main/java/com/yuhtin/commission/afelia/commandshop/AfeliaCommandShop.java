package com.yuhtin.commission.afelia.commandshop;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.yuhtin.commission.afelia.commandshop.api.BuyableCommand;
import com.yuhtin.commission.afelia.commandshop.cache.ShopCache;
import com.yuhtin.commission.afelia.commandshop.command.ShopCommand;
import com.yuhtin.commission.afelia.commandshop.hook.EconomyHook;
import com.yuhtin.commission.afelia.commandshop.utils.ColorUtil;
import com.yuhtin.commission.afelia.commandshop.utils.NumberUtils;
import com.yuhtin.commission.afelia.commandshop.view.ViewRegistry;
import lombok.Getter;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AfeliaCommandShop extends JavaPlugin {

    private final ViewRegistry viewRegistry = new ViewRegistry();
    private final ShopCache shopCache = new ShopCache();

    private final EconomyHook economyHook = new EconomyHook();

    @Override
    public void onEnable() {
        InventoryManager.enable(this);

        saveDefaultConfig();
        economyHook.init();

        val itemLore = getConfig().getStringList("itemLore");
        for (String key : getConfig().getConfigurationSection("commands").getKeys(false)) {
            val price = getConfig().getDouble("commands." + key + ".price");
            val name = ColorUtil.colored(getConfig().getString("commands." + key + ".name"));
            val material = Material.valueOf(getConfig().getString("commands." + key + ".material"));
            val permission = getConfig().getString("commands." + key + ".permission");

            val itemStack = new ItemStack(material);
            val itemMeta = itemStack.getItemMeta();

            List<String> lore = new ArrayList<>();
            for (String s : itemLore) {
                lore.add(ColorUtil.colored(s).replace("@price", NumberUtils.format(price)));
            }

            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            shopCache.getCommands().add(new BuyableCommand(itemStack, permission, price));
            getLogger().info("Loaded " + key + " command");
        }

        viewRegistry.init(this);
        getCommand("commandshop").setExecutor(new ShopCommand(this));
    }

    public static AfeliaCommandShop getInstance() {
        return getPlugin(AfeliaCommandShop.class);
    }
}
