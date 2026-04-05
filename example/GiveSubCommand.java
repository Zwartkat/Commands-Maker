package fr.zwartkat.commandsmaker.example;

import fr.zwartkat.commandsmaker.core.base.parameters.Parameter;
import fr.zwartkat.commandsmaker.core.base.parameters.ParameterMap;
import fr.zwartkat.commandsmaker.core.base.parameters.ParameterMapFactory;
import fr.zwartkat.commandsmaker.core.base.parameters.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiveSubCommand extends SubCommand {
    public GiveSubCommand() {
        super("give", "Exemple give", "/example give <player> <item> <amount>", new ArrayList<>(), "example.command.give", ParameterMapFactory.argumentMap());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        // You must always check arguments provided by the user (that can be length or the content of args)
        // Subcommands aren't in args
        if(args.length != 3) {
            sender.sendMessage("Arguments are invalid");
            return false;
        }

        Player playerToGive = Bukkit.getPlayer(args[0]);
        if(playerToGive == null) {
            sender.sendMessage(ChatColor.RED + "Specified player is unknown");
            return false;
        }

        Material item = Material.matchMaterial(args[1]);
        if(item == null){
            sender.sendMessage(ChatColor.RED + "Specified item is unknown");
            return false;
        }

        Integer amount = (Integer) Integer.parseInt(args[2]);

        ItemStack itemStack = new ItemStack(item, amount);

        playerToGive.getInventory().addItem(itemStack);

        sender.sendMessage(ChatColor.GREEN + "You have given " + itemStack.getAmount() + "x" + itemStack.getType() + " to " + playerToGive.getName());
        playerToGive.sendMessage(ChatColor.GREEN + "You received " + itemStack.getAmount() + "x" + itemStack.getType());

        return true;
    }
}
