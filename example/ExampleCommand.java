package fr.zwartkat.commandsmaker.example;

import fr.zwartkat.commandsmaker.core.base.Command;
import fr.zwartkat.commandsmaker.core.base.Completer;
import fr.zwartkat.commandsmaker.core.base.parameters.Argument;
import fr.zwartkat.commandsmaker.core.base.parameters.Parameter;
import fr.zwartkat.commandsmaker.core.base.parameters.ParameterMapFactory;
import fr.zwartkat.commandsmaker.core.base.parameters.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;

public class ExampleCommand extends Command {


    public ExampleCommand() {
        // This command must be registered into plugin.yml
        super("example", "An example command","/example <subcommand>", List.of("ex","examples"),"example.command", ParameterMapFactory.subCommandMap());

        // Create a subcommand
        SubCommand giveSubCommand = new SubCommand("give", "Exemple give", "/example give <player> <item> <amount>", new ArrayList<>(), "example.command.give", ParameterMapFactory.argumentMap());

        // Add an argument to show all online players
        giveSubCommand.addParameter(new Argument("player_to_give",Completer.getPlayers(true)));
        // Add an argument to show all items
        giveSubCommand.addParameter(new Argument("items", Completer.getAllItems()));
        // Add an argument to show amount to give
        giveSubCommand.addParameter(new Argument("amount", Completer.generatePowersOf(0,10))); // This use a list of number but there is Completer value for this
        // Define command action
        giveSubCommand.setAction(this::giveItem);

        // Create a  subcommand
        SubCommand flySubCommand = new SubCommand("fly","example.command.fly","enable/disable fly","example.command.fly",ParameterMapFactory.argumentMap());
        flySubCommand.addParameter(new Argument("enable",Completer.getBoolean()));
        flySubCommand.setAction(this::fly);


        this.addParameter(giveSubCommand);
        this.addParameter(flySubCommand);

    }

    private void giveItem(CommandSender sender, String label, String[] args){

        // You must always check arguments provided by the user (that can be length or the content of args)
        // Subcommands aren't in args
        if(args.length != 3) {
            sender.sendMessage("Arguments are invalid");
            return;
        }

        Player playerToGive = Bukkit.getPlayer(args[0]);
        if(playerToGive == null) {
            sender.sendMessage(ChatColor.RED + "Specified player is unknown");
            return;
        }

        Material item = Material.matchMaterial(args[1]);
        if(item == null){
            sender.sendMessage(ChatColor.RED + "Specified item is unknown");
            return;
        }

        Integer amount = (Integer) Integer.parseInt(args[2]);

        ItemStack itemStack = new ItemStack(item, amount);

        playerToGive.getInventory().addItem(itemStack);

        sender.sendMessage(ChatColor.GREEN + "You have given " + itemStack.getAmount() + "x" + itemStack.getType() + " to " + playerToGive.getName());
        playerToGive.sendMessage(ChatColor.GREEN + "You received " + itemStack.getAmount() + "x" + itemStack.getType());

    }

    private void fly(CommandSender sender, String label, String[] args){

        if(args.length != 1) {
            sender.sendMessage("Arguments are invalid");
            return;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage("You must be a player to execute this command");
            return;
        }

        Player player = (Player) sender;

        if(args[0].equalsIgnoreCase("true")){
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("You fly now");
        }
        else if(args[0].equalsIgnoreCase("false")){
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage("You can't fly anymore");
        }
        else {
            player.sendMessage("You must specify true or false");
            return;
        }
    }
}
