package com.yuhtin.commission.afelia.commandshop.manager;

import com.yuhtin.commission.afelia.commandshop.api.BuyableCommand;
import com.yuhtin.commission.afelia.commandshop.api.BuyableRank;
import com.yuhtin.commission.afelia.commandshop.api.ConfigurableShop;
import com.yuhtin.commission.afelia.commandshop.cache.ShopCache;
import com.yuhtin.commission.afelia.commandshop.utils.ColorUtil;
import com.yuhtin.commission.afelia.commandshop.utils.NumberUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ShopManager {

    @Getter private final ShopCache shopCache = new ShopCache();

    public void register(ConfigurableShop shop) {
        val section = shop.getItemSection();
        for (String key : section.getKeys(false)) {
            val name = ColorUtil.colored(section.getString(key + ".name", "&aTest"));
            val material = Material.valueOf(section.getString(key + ".material", "FEATHER"));
            val price = section.getDouble(key + ".price", 0);
            val permission = section.getString(key + ".permission", "");
            val rankParent = section.getString(key + ".rankParent", "");

            val itemStack = new ItemStack(material);
            val itemMeta = itemStack.getItemMeta();

            List<String> lore = new ArrayList<>();
            for (String s : shop.getItemLore()) {
                lore.add(ColorUtil.colored(s).replace("@price", NumberUtils.format(price)));
            }

            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            if (shop.getType().equalsIgnoreCase("rank")) {
                shopCache.getRanks().add(new BuyableRank(itemStack, rankParent, permission, price));
            } else {
                shopCache.getCommands().add(new BuyableCommand(itemStack, permission, price));
            }
        }
    }

}
