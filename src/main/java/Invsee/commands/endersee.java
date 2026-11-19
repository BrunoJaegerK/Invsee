package Invsee.commands;

import Invsee.Main;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;
import me.iwareq.fakeinventories.FakeInventory;

import java.util.Map;

public class endersee extends Command {

    private final Main plugin;

    public endersee(Main plugin) {
        super(
                "endersee",
                plugin.getInvseeConfig().ecdescription(),
                plugin.getInvseeConfig().ecusagemessage(),
                new String[]{"ecsee"}
        );
        this.plugin = plugin;
        setPermission("endersee.cmd");
        this.commandParameters.put(
                "endersee",
                new CommandParameter[]{new CommandParameter("endersee", CommandParamType.TARGET, false)}
        );
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getInvseeConfig().isNotaPlayer());
            return true;
        }

        if (!sender.hasPermission("endersee.cmd") && !sender.isOp()) {
            sender.sendMessage(prefix() + plugin.getInvseeConfig().hasNotPermission());
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(prefix() + plugin.getInvseeConfig().ecusagemessage());
            return true;
        }

        Player viewer = (Player) sender;
        Player target = Server.getInstance().getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(prefix() + plugin.getInvseeConfig().isNotOnline());
            return true;
        }

        if (target.getName().equalsIgnoreCase(viewer.getName())) {
            sender.sendMessage(prefix() + "§cYou can't edit your own ender chest!");
            return true;
        }

        // Wait 10 server ticks (approximately 0.5 seconds) before opening the fake inventory.
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int currentTick) {
                if (!viewer.isOnline() || !target.isOnline()) {
                    return;
                }

                FakeInventory inventory = new FakeInventory(
                        InventoryType.ENDER_CHEST,
                        "§e" + target.getName() + "'s §5enderchest!"
                );
                inventory.setContents(target.getEnderChestInventory().getContents());

                inventory.setDefaultItemHandler((item, event) ->
                        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                            @Override
                            public void onRun(int tick) {
                                if (target.isOnline()) {
                                    Map<Integer, Item> contents = inventory.getContents();
                                    target.getEnderChestInventory().setContents(contents);
                                }
                            }
                        }, 1)
                );

                viewer.addWindow(inventory);
            }
        }, 10);

        return true;
    }

    private String prefix() {
        return plugin.getInvseeConfig().prefix();
    }
}
