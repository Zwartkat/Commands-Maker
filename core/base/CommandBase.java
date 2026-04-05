package fr.zwartkat.commandsmaker.core.base;

import fr.zwartkat.commandsmaker.core.base.parameters.Parameter;
import fr.zwartkat.commandsmaker.core.base.parameters.ParameterMap;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class CommandBase<T extends Parameter> implements ExecutableCommand<T>{
    private final String name,description,usage, permission;
    private final List<String> aliases;
    protected ParameterMap<T> parameterMap;

    public CommandBase(String name, String description, String usage, List<String> aliases, String permission){
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
        this.aliases = aliases;
    }

    public String getName() {
        return this.name;
    }

    public String getUsage(){
        return this.usage;
    }

    public String getDescription(){
        return this.description;
    }

    public String getPermission(){
        return this.permission;
    }

    public List<String> getAliases(){
        return this.aliases;
    }

    public ParameterMap<T> getParameterList(){
        return this.parameterMap;
    }

    public void setParameterList(ParameterMap<T> parameters){
        this.parameterMap = parameters;
    }

}
