package fr.zwartkat.zstaff.commands;

import fr.zwartkat.zstaff.commandsmaker.CommandBase;
import fr.zwartkat.zstaff.commandsmaker.Completer;
import fr.zwartkat.zstaff.commandsmaker.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ExampleCommand extends CommandBase {


    public ExampleCommand() {
        // This command must be registered into plugin.yml
        super("example", "An example command", "example.command","/example <subcommand>", List.of("ex","examples"));

        // Create a subcommand
        SubCommand giveSubCommand = new SubCommand("give", "example.command.give", "Example give command");

        // Add an argument to show all online players
        giveSubCommand.addArg("player_to_give", Completer.getOnlinePlayers());
        // Add an argument to show all items
        giveSubCommand.addArg("item_to_give", Completer.getAllItems());
        // Add an argument to show amount to give
        giveSubCommand.addArg("amount", List.of("1","2","3","4")); // This use a list of number but there is Completer value for this
        // Define command action
        giveSubCommand.setAction(this::giveItem);

        // Create a  subcommand
        SubCommand flySubCommand = new SubCommand("fly","example.command.fly","enable/disable fly");
        flySubCommand.addArg("enable",List.of("true","false"));
        flySubCommand.setAction(this::fly);


        this.addSubCommand(giveSubCommand);
        this.addSubCommand(flySubCommand);

    }

    private void giveItem(CommandSender sender, String label, String[] args){

        // You must always check arguments provided by the user (that can be length or the content of args)
        // Subcommands aren't in args here
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

        Integer amount = Integer.parseInt(args[2]);

        ItemStack itemStack = new ItemStack(item, amount);

        playerToGive.getInventory().addItem(itemStack);

        sender.sendMessage(ChatColor.GREEN + "You have given " + itemStack.getAmount() + "x" + itemStack.getType() + " to " + playerToGive.getName());
        playerToGive.sendMessage(ChatColor.GREEN + "You received " + itemStack.getAmount() + "x" + itemStack.getType());

    }

    private void fly(CommandSender sender, String label, String args[]){

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
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage("You can't fly anymore");
        }
        else {
            player.sendMessage("You must specify true or false");
            return;
        }
    }
}
package fr.zwartkat.zstaff.commands;

import fr.zwartkat.zstaff.commandsmaker.CommandBase;
import fr.zwartkat.zstaff.commandsmaker.Completer;
import fr.zwartkat.zstaff.commandsmaker.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ExampleCommand extends CommandBase {


    public ExampleCommand() {
        // This command must be registered into plugin.yml
        super("example", "An example command", "example.command","/example <subcommand>", List.of("ex","examples"));

        // Create a subcommand
        SubCommand giveSubCommand = new SubCommand("give", "example.command.give", "Example give command");

        // Add an argument to show all online players
        giveSubCommand.addArg("player_to_give", Completer.getOnlinePlayers());
        // Add an argument to show all items
        giveSubCommand.addArg("item_to_give", Completer.getAllItems());
        // Add an argument to show amount to give
        giveSubCommand.addArg("amount", List.of("1","2","3","4")); // This use a list of number but there is Completer value for this
        // Define command action
        giveSubCommand.setAction(this::giveItem);

        // Create a  subcommand
        SubCommand flySubCommand = new SubCommand("fly","example.command.fly","enable/disable fly");
        flySubCommand.addArg("enable",List.of("true","false"));
        flySubCommand.setAction(this::fly);


        this.addSubCommand(giveSubCommand);
        this.addSubCommand(flySubCommand);

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

        Integer amount = Integer.parseInt(args[2]);

        ItemStack itemStack = new ItemStack(item, amount);

        playerToGive.getInventory().addItem(itemStack);

        sender.sendMessage(ChatColor.GREEN + "You have given " + itemStack.getAmount() + "x" + itemStack.getType() + " to " + playerToGive.getName());
        playerToGive.sendMessage(ChatColor.GREEN + "You received " + itemStack.getAmount() + "x" + itemStack.getType());

    }

    private void fly(CommandSender sender, String label, String args[]){

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
