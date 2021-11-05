package com.yuhtin.commission.afelia.commandshop.command;

import com.yuhtin.commission.afelia.commandshop.AfeliaCommandShop;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class ShopCommand implements CommandExecutor {

    private final AfeliaCommandShop afeliaCommandShop;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            val shopView = afeliaCommandShop.getViewRegistry().getShopView();
            shopView.openInventory(((Player) sender));
        }

        return false;
    }
}
