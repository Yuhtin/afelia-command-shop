package com.yuhtin.commission.afelia.commandshop.cache;

import com.yuhtin.commission.afelia.commandshop.api.BuyableCommand;
import com.yuhtin.commission.afelia.commandshop.api.BuyableRank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShopCache {

    private final List<BuyableCommand> commands = new ArrayList<>();
    private final List<BuyableRank> ranks = new ArrayList<>();

}
