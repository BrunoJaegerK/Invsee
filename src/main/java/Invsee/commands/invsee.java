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

import java.util.HashMap;
import java.util.Map;

public class invsee extends Command {

    private final Main plugin;

    public invsee(Main plugin) {
        super(
                "invsee",
                plugin.getInvseeConfig().invdescription(),
                plugin.getInvseeConfig().usagemessage(),
                new String[]{"inv"}
        );
        this.plugin = plugin;
        setPermission("invsee.cmd");
        this.commandParameters.put(
                "invsee",
                new CommandParameter[]{new CommandParameter("invsee", CommandParamType.TARGET, false)}
        );
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getInvseeConfig().isNotaPlayer());
            return true;
        }

        if (!sender.hasPermission("invsee.cmd") && !sender.isOp()) {
            sender.sendMessage(prefix() + plugin.getInvseeConfig().hasNotPermission());
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(prefix() + plugin.getInvseeConfig().usagemessage());
            return true;
        }

        Player viewer = (Player) sender;
        Player target = Server.getInstance().getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(prefix() + plugin.getInvseeConfig().isNotOnline());
            return true;
        }

        if (target.getName().equalsIgnoreCase(viewer.getName())) {
            sender.sendMessage(prefix() + "§cYou can't edit your own inventory!");
            return true;
        }

        final FakeInventory inventory = new FakeInventory(
                InventoryType.DOUBLE_CHEST,
                "§e" + target.getName() + "'s §3inventory!"
        );
        inventory.setContents(target.getInventory().getContents());

        // The lower slots are reserved for display and cannot be taken.
        for (int slot = 40; slot < 54; slot++) {
            inventory.setItem(slot, Item.get(-161), (item, event) -> event.setCancelled(true));
        }

        // FakeInventories-1 dispatches InventoryTransactionEvent to the item handler.
        // Each menu captures its own target; no shared static state is used.
        inventory.setDefaultItemHandler((item, event) ->
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    @Override
                    public void onRun(int currentTick) {
                        if (target.isOnline()) {
                            Map<Integer, Item> contents = new HashMap<>();
                            for (Map.Entry<Integer, Item> entry : inventory.getContents().entrySet()) {
                                if (entry.getKey() < 40) {
                                    contents.put(entry.getKey(), entry.getValue());
                                }
                            }
                            target.getInventory().setContents(contents);
                        }
                    }
                }, 1)
        );

        viewer.addWindow(inventory);
        return true;
    }

    private String prefix() {
        return plugin.getInvseeConfig().prefix();
    }
}
