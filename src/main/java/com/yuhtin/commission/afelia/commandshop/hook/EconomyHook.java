package com.yuhtin.commission.afelia.commandshop.hook;

import com.yuhtin.commission.afelia.tokensystem.AfeliaTokenSystem;
import com.yuhtin.commission.afelia.tokensystem.api.account.Account;
import com.yuhtin.commission.afelia.tokensystem.api.account.storage.AccountStorage;
import org.bukkit.OfflinePlayer;

public final class EconomyHook {

    private AccountStorage accountStorage;

    public void init() {
        accountStorage = AfeliaTokenSystem.getInstance().getAccountStorage();
    }

    public Account getPlayer(OfflinePlayer player) {
        return accountStorage.findAccount(player);
    }

}
