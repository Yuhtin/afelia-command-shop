package com.yuhtin.commission.afelia.commandshop.api;

import com.yuhtin.commission.afelia.commandshop.utils.ColorUtil;
import lombok.Data;
import lombok.val;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@Data
public class ConfigurableShop {

    private final ConfigurationSection itemSection;

    private final String type;
    private final String inventoryName;
    private final String success;
    private final String noCoins;
    private final String executionCommand;
    private final List<String> itemLore;

    public ConfigurableShop(FileConfiguration config, String path) {
        val section = config.getConfigurationSection("shops." + path + ".config");

        executionCommand = section.getString("executionCommand");
        success = ColorUtil.colored(section.getString("success"));
        noCoins = ColorUtil.colored(section.getString("nocoins"));
        inventoryName = ColorUtil.colored(section.getString("inventoryName"));
        itemLore = section.getStringList("itemLore");
        itemSection = config.getConfigurationSection("shops." + path + ".items");

        type = path;
    }

}
