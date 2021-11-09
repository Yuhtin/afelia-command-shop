package com.yuhtin.commission.afelia.commandshop;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.yuhtin.commission.afelia.commandshop.command.CommandShopCommand;
import com.yuhtin.commission.afelia.commandshop.command.RankShopCommand;
import com.yuhtin.commission.afelia.commandshop.hook.EconomyHook;
import com.yuhtin.commission.afelia.commandshop.manager.ShopManager;
import com.yuhtin.commission.afelia.commandshop.view.registry.ViewRegistry;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AfeliaCommandShop extends JavaPlugin {

    private final ViewRegistry viewRegistry = new ViewRegistry();
    private final ShopManager shopManager = new ShopManager();

    private final EconomyHook economyHook = new EconomyHook();

    @Override
    public void onEnable() {
        InventoryManager.enable(this);

        saveDefaultConfig();
        economyHook.init();

        viewRegistry.init(this);
        getCommand("commandshop").setExecutor(new CommandShopCommand(this));
        getCommand("rankshop").setExecutor(new RankShopCommand(this));

        getLogger().info("Registered " + shopManager.getShopCache().getRanks().size() + " buyable ranks.");
        getLogger().info("Registered " + shopManager.getShopCache().getCommands().size() + " buyable commands.");
    }

    public static AfeliaCommandShop getInstance() {
        return getPlugin(AfeliaCommandShop.class);
    }
}
