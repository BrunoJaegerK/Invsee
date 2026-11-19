package Invsee;

import Invsee.commands.endersee;
import Invsee.commands.invsee;
import Invsee.utils.InvseeConfig;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.Plugin;

public class Main extends PluginBase {

    private InvseeConfig invseeConfig;

    @Override
    public void onEnable() {
        Plugin fakeInventories = getServer().getPluginManager().getPlugin("FakeInventories");
        if (fakeInventories == null || !fakeInventories.isEnabled()) {
            getLogger().error("FakeInventories is required. Install the PM1E-compatible FakeInventories build before enabling Invsee.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.invseeConfig = new InvseeConfig(this);
        this.invseeConfig.createDefault();

        getServer().getCommandMap().register("invsee", new invsee(this));
        getServer().getCommandMap().register("endersee", new endersee(this));

        getLogger().info(invseeConfig.prefix() + "InvseePM1E has been enabled.");
    }

    @Override
    public void onDisable() {
        if (invseeConfig != null) {
            getLogger().info(invseeConfig.prefix() + "InvseePM1E has been disabled.");
        }
    }

    public InvseeConfig getInvseeConfig() {
        return invseeConfig;
    }
}
