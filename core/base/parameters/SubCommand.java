package fr.zwartkat.commandsmaker.core.base.parameters;

import fr.zwartkat.commandsmaker.core.base.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

abstract public class SubCommand extends Command implements Parameter {

    public SubCommand(String name, String description, String usage, List<String> aliases,String permission, ParameterMap<? extends Parameter> parameterMap){
        super(name,description,usage,aliases,permission,parameterMap);
    }
    public SubCommand(String name, String description, String usage,String permission, ParameterMap<? extends Parameter> parameterMap){
        super(name,description,usage,List.of(),permission,parameterMap);
    }
    public SubCommand(String name,String permission, ParameterMap<? extends Parameter> parameterMap){
        super(name,"","",List.of(),permission,parameterMap);
    }

    @Override
    public String getKey() {
        return this.getName();
    }

    @Override
    public boolean isExecutable() {
        return true;
    }

}
