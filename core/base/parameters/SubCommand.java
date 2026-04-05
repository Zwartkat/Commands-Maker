package fr.zwartkat.commandsmaker.core.base.parameters;

import fr.zwartkat.commandsmaker.core.base.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class SubCommand extends Command implements Parameter {

    public SubCommand(String name, String description, String usage, List<String> aliases,String permission, ParameterMap<? extends Parameter> parameterMap){
        super(name,description,usage,aliases,permission,parameterMap);
    }
    public SubCommand(String name, String description, String usage,String permission, ParameterMap<? extends Parameter> parameterMap){
        super(name,description,usage,List.of(),permission,parameterMap);
    }
    public SubCommand(String name,String permission, ParameterMap<? extends Parameter> parameterMap){
        super(name,"","",List.of(),permission,parameterMap);
    }
    /**
     * Execute the action of the subcommand
     * @param sender The sender of the command
     * @param label The command name
     * @param args The arguments of the command
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args){
        if(this.action == null) {
            return false;
        }
        if(this.hasPermission(sender)){
            try{
                this.action.apply(sender, label, args);
            }
            catch(Exception e){
                e.printStackTrace();
                sender.sendMessage("§cAn error occurred while trying to use this command, please contact administrator");
                return false;
            }
            return true;
        }
        else {
            sender.sendMessage("§cYou don't have the permission to execute this command");
            return false;
        }
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
