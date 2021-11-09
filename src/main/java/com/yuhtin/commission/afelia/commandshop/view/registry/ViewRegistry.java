package com.yuhtin.commission.afelia.commandshop.view.registry;

import com.yuhtin.commission.afelia.commandshop.AfeliaCommandShop;
import com.yuhtin.commission.afelia.commandshop.api.ConfigurableShop;
import com.yuhtin.commission.afelia.commandshop.view.CommandShopView;
import com.yuhtin.commission.afelia.commandshop.view.RankShopView;
import lombok.Getter;
import lombok.val;

@Getter
public class ViewRegistry {

    private RankShopView rankShopView;
    private CommandShopView commandShopView;

    public void init(AfeliaCommandShop afeliaCommandShop) {
        val config = afeliaCommandShop.getConfig();
        val shopManager = afeliaCommandShop.getShopManager();
        val economyHook = afeliaCommandShop.getEconomyHook();
        val shopCache = shopManager.getShopCache();

        val rankConfig = new ConfigurableShop(config, "rank");
        val commandConfig = new ConfigurableShop(config, "command");

        shopManager.register(rankConfig);
        shopManager.register(commandConfig);

        rankShopView = new RankShopView(shopCache, economyHook, rankConfig).init();
        commandShopView = new CommandShopView(shopCache, economyHook, commandConfig).init();
    }



}
