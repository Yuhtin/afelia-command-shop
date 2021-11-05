package com.yuhtin.commission.afelia.commandshop.cache;

import com.yuhtin.commission.afelia.commandshop.api.BuyableCommand;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShopCache {

    private final List<BuyableCommand> commands = new ArrayList<>();

}
