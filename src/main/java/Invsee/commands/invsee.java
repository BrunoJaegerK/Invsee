package Invsee.commands;

import Invsee.Main;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;
import com.nukkitx.fakeinventories.inventory.DoubleChestFakeInventory;
import com.nukkitx.fakeinventories.inventory.FakeSlotChangeEvent;

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

        final DoubleChestFakeInventory inventory = new DoubleChestFakeInventory();
        inventory.setName("§e" + target.getName() + "'s §3inventory!");
        inventory.setContents(target.getInventory().getContents());

        // Keep the lower rows reserved, as in the original plugin.
        for (int slot = 40; slot < 54; slot++) {
            inventory.setItem(slot, Item.get(-161));
        }

        // The listener belongs to this specific window and captures its target.
        // No shared static target means two staff members cannot cross-edit players.
        inventory.addListener(event -> onSlotChange(event, inventory, target));
        viewer.addWindow(inventory);
        return true;
    }

    private void onSlotChange(
            FakeSlotChangeEvent event,
            DoubleChestFakeInventory inventory,
            Player target
    ) {
        if (event.getInventory() != inventory) {
            return;
        }

        if (event.getAction().getSlot() >= 40) {
            event.setCancelled(true);
            return;
        }

        // Nukkit applies the transaction after the slot-change callback.
        // Delay the copy by one tick so the fake inventory contains the new state.
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int currentTick) {
                if (target.isOnline()) {
                    Map<Integer, Item> contents = new java.util.HashMap<>();
                    for (Map.Entry<Integer, Item> entry : inventory.getContents().entrySet()) {
                        // Slots 40-53 are display-only filler slots in this fake window.
                        if (entry.getKey() < 40) {
                            contents.put(entry.getKey(), entry.getValue());
                        }
                    }
                    target.getInventory().setContents(contents);
                }
            }
        }, 1);
    }

    private String prefix() {
        return plugin.getInvseeConfig().prefix();
    }
}
