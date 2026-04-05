package fr.zwartkat.commandsmaker.example;

import fr.zwartkat.commandsmaker.core.base.Command;
import fr.zwartkat.commandsmaker.core.base.Completer;
import fr.zwartkat.commandsmaker.core.base.parameters.*;
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
        SubCommand giveSubCommand = new GiveSubCommand();
        // Create a  subcommand
        SubCommand flySubCommand = new FlySubCommand();

        // Add subcommand to this command
        this.addParameter(giveSubCommand);
        this.addParameter(flySubCommand);

    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return false;
    }
}
