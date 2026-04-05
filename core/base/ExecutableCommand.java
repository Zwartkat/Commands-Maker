package fr.zwartkat.commandsmaker.core.base;

import fr.zwartkat.commandsmaker.core.base.parameters.Parameter;
import fr.zwartkat.commandsmaker.core.base.parameters.ParameterMap;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ExecutableCommand<T extends Parameter> {

    public List<String> getTabCompleter(CommandSender sender, String[] args, int argIndex);
    public boolean execute(CommandSender sender, String label, String[] args);

}
