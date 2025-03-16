package fr.zwartkat.commandsmaker;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface SubCommandBase extends ExecutableCommand {

    public String getName();
    public String getDescription();
    public String getUsage();
    public String getPermission();
    public HashMap<String,SubCommandBase> getSubCommands();
    public LinkedHashMap<String, List<String>> getSubCommandArguments();
    public SubCommandBase setCommandName(String name);
    public SubCommandBase setCommandDescription(String description);

    public SubCommandBase setCommandUsage(String message);

    public SubCommandBase setCommandPermission(String permission);
}
