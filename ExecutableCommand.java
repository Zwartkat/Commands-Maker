package fr.zwartkat.commandsmaker;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ExecutableCommand {

    public void addSubCommand(SubCommandBase subCommand);
    public void addArg(String argKey, List<String> tabCompleterArgs);
    public void setAction(SubCommandExecutionInterface<CommandSender,String,String[]> action);
    public List<String> getTabCompleter(CommandSender sender, String[] args, Integer argIndex);
    public boolean execute(CommandSender sender, String label, String[] args);

}
