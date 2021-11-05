package com.yuhtin.commission.afelia.commandshop.view;

import com.yuhtin.commission.afelia.commandshop.AfeliaCommandShop;
import com.yuhtin.commission.afelia.commandshop.utils.ColorUtil;
import lombok.Getter;
import lombok.val;

@Getter
public class ViewRegistry {

    private ShopView shopView;

    public void init(AfeliaCommandShop afeliaCommandShop) {
        val config = afeliaCommandShop.getConfig();
        shopView = new ShopView(
                afeliaCommandShop.getShopCache(),
                afeliaCommandShop.getEconomyHook(),
                config.getString("permissionCommand"),
                ColorUtil.colored(config.getString("success")),
                ColorUtil.colored(config.getString("noCoins")),
                ColorUtil.colored(config.getString("inventoryName"))
        ).init();
    }

}
