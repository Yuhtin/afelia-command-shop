package com.yuhtin.commission.afelia.commandshop.view;

import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.enums.DefaultItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.yuhtin.commission.afelia.commandshop.api.BuyableCommand;
import com.yuhtin.commission.afelia.commandshop.cache.ShopCache;
import com.yuhtin.commission.afelia.commandshop.hook.EconomyHook;
import com.yuhtin.commission.afelia.commandshop.utils.NumberUtils;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShopView extends PagedInventory {

    private final ShopCache shopCache;
    private final EconomyHook economy;

    private final String permissionCommand;
    private final String success;
    private final String noCoinsMessage;

    public ShopView(ShopCache shopCache, EconomyHook economy, String permissionCommand, String success, String noCoinsMessage, String inventoryName) {
        super("shopcommand.main", inventoryName, 6 * 9);

        this.shopCache = shopCache;
        this.permissionCommand = permissionCommand;
        this.success = success;
        this.noCoinsMessage = noCoinsMessage;
        this.economy = economy;
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(@NotNull PagedViewer viewer) {
        val items = new ArrayList<InventoryItemSupplier>();
        for (BuyableCommand command : shopCache.getCommands()) {
            if (viewer.getPlayer().hasPermission(command.getPermission())) continue;

            items.add(() -> InventoryItem.of(command.getItem()).defaultCallback(callback -> {
                val player = callback.getPlayer();
                if (!economy.has(player, command.getPrice())) {
                    player.sendMessage(noCoinsMessage);
                    return;
                }

                val economyResponse = economy.withdrawCoins(player, command.getPrice());
                if (!economyResponse.transactionSuccess()) {
                    player.sendMessage(noCoinsMessage);
                    return;
                }

                player.sendMessage(success.replace("@coins", NumberUtils.format(command.getPrice())));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), permissionCommand
                        .replace("@player", player.getName())
                        .replace("@perm", command.getPermission())
                );

                callback.updateInventory();
            }));
        }

        return items;
    }
}
