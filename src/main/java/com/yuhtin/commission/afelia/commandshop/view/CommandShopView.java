package com.yuhtin.commission.afelia.commandshop.view;

import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.yuhtin.commission.afelia.commandshop.api.BuyableCommand;
import com.yuhtin.commission.afelia.commandshop.api.ConfigurableShop;
import com.yuhtin.commission.afelia.commandshop.cache.ShopCache;
import com.yuhtin.commission.afelia.commandshop.hook.EconomyHook;
import com.yuhtin.commission.afelia.commandshop.utils.NumberUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandShopView extends PagedInventory {

    private final ShopCache shopCache;
    private final EconomyHook economy;

    private final ConfigurableShop configurableShop;

    public CommandShopView(ShopCache shopCache, EconomyHook economy, ConfigurableShop configurableShop) {
        super("shopcommand.main", configurableShop.getInventoryName(), 6 * 9);

        this.shopCache = shopCache;
        this.economy = economy;

        this.configurableShop = configurableShop;
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(@NotNull PagedViewer viewer) {
        val items = new ArrayList<InventoryItemSupplier>();
        for (BuyableCommand command : shopCache.getCommands()) {
            if (viewer.getPlayer().hasPermission(command.getPermission())) continue;

            items.add(() -> InventoryItem.of(command.getItem()).defaultCallback(callback -> {
                val player = callback.getPlayer();
                val account = economy.getPlayer(player);
                if (account.getBalance() < command.getPrice()) {
                    player.sendMessage(configurableShop.getNoCoins());
                    return;
                }

                account.setBalance(account.getBalance() - command.getPrice());

                player.sendMessage(configurableShop.getSuccess().replace("@coins", NumberUtils.format(command.getPrice())));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), configurableShop.getExecutionCommand()
                        .replace("@player", player.getName())
                        .replace("@perm", command.getPermission())
                );

                callback.updateInventory();
            }));
        }

        return items;
    }
}
