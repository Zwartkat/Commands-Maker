package fr.zwartkat.commandsmaker.example;

import fr.zwartkat.commandsmaker.core.base.Completer;
import fr.zwartkat.commandsmaker.core.base.parameters.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FlySubCommand extends SubCommand {
    public FlySubCommand() {
        super("fly","example.command.fly","enable/disable fly","example.command.fly", ParameterMapFactory.argumentMap());

        this.addParameter(new Argument("enable",Completer.getBoolean()));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if(args.length != 1) {
            sender.sendMessage("Arguments are invalid");
            return false;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage("You must be a player to execute this command");
            return false;
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
            return false;
        }

        return true;
    }
}
