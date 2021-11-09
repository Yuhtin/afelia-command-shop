package com.yuhtin.commission.afelia.commandshop.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public class BuyableRank {

    private final ItemStack item;
    private final String rankName;
    private final String permission;
    private final double price;

}
