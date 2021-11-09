package com.yuhtin.commission.afelia.commandshop.view;

import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.yuhtin.commission.afelia.commandshop.api.BuyableRank;
import com.yuhtin.commission.afelia.commandshop.api.ConfigurableShop;
import com.yuhtin.commission.afelia.commandshop.cache.ShopCache;
import com.yuhtin.commission.afelia.commandshop.hook.EconomyHook;
import com.yuhtin.commission.afelia.commandshop.utils.NumberUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RankShopView extends PagedInventory {

    private final ShopCache shopCache;
    private final EconomyHook economy;

    private final ConfigurableShop configurableShop;

    public RankShopView(ShopCache shopCache, EconomyHook economy, ConfigurableShop configurableShop) {
        super("rankcommand.main", configurableShop.getInventoryName(), 6 * 9);

        this.shopCache = shopCache;
        this.economy = economy;
        this.configurableShop = configurableShop;
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(@NotNull PagedViewer viewer) {
        val items = new ArrayList<InventoryItemSupplier>();
        for (BuyableRank rank : shopCache.getRanks()) {
            if (viewer.getPlayer().hasPermission(rank.getPermission())) continue;

            items.add(() -> InventoryItem.of(rank.getItem()).defaultCallback(callback -> {
                val player = callback.getPlayer();
                if (!economy.has(player, rank.getPrice())) {
                    player.sendMessage(configurableShop.getNoCoins());
                    return;
                }

                val economyResponse = economy.withdrawCoins(player, rank.getPrice());
                if (!economyResponse.transactionSuccess()) {
                    player.sendMessage(configurableShop.getNoCoins());
                    return;
                }

                player.sendMessage(configurableShop.getSuccess().replace("@coins", NumberUtils.format(rank.getPrice())));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), configurableShop.getExecutionCommand()
                        .replace("@player", player.getName())
                        .replace("@rank", rank.getRankName())
                );

                callback.updateInventory();
            }));
        }

        return items;
    }
}
