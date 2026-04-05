package fr.zwartkat.commandsmaker.core.base;

import fr.zwartkat.commandsmaker.core.base.parameters.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class Command extends CommandBase<Parameter> implements CommandExecutor, TabExecutor {

    protected ExecutionInterface<CommandSender,String, String[]> action;

    public Command(String name, String description, String usage, List<String> aliases, String permission, ParameterMap<? extends Parameter> parameterMap) {
        super(name, description,usage, aliases, permission);
        this.parameterMap = (ParameterMap<Parameter>) parameterMap;
        this.action = null;
    }

    @Override
    public void addParameter(Parameter param) {
        this.parameterMap.add(param);
    }

    @Override
    public void setParameters(ParameterMap<Parameter> parameterMap) {
        this.parameterMap = parameterMap;
    }


    /**
     * Define the action of the subcommand
     * @param action The action to execute
     */
    public void setAction(ExecutionInterface<CommandSender, String, String[]> action){
        this.action = action;
    }

    public boolean hasPermission(CommandSender sender){
        return sender.hasPermission(this.getPermission());
    }

    public List<String> getArgTabCompleter(List<String> completer, String[] args, int index){
        if ((args.length - index) <= this.parameterMap.size()) {
            Iterator<Map.Entry<String, Parameter>> iterator = this.parameterMap.getAll().entrySet().iterator();
            int currentIndex = 1;
            while (iterator.hasNext()) {
                Map.Entry<String, Parameter> entry = iterator.next();

                if (currentIndex == args.length - index) {
                    completer.addAll(((Argument) entry.getValue()).getValues());
                    break;
                }
                currentIndex++;
            }
        }
        return completer;
    }

    public List<String> getSubCommandTabCompleter(List<String> completer,CommandSender sender, String[] args, int index){
        if (args.length == index + 1) {

            this.parameterMap.getAll().forEach((name, subCommand) -> {
                if (this.hasPermission(sender)) {
                    completer.add(name);
                }
            });
        }
        // Else take the completer of used subcommands
        else {
            if (this.parameterMap.getAll().containsKey(args[index])) {
                SubCommand subCommand = (SubCommand) this.parameterMap.getAll().get(args[index]);
                completer.addAll(subCommand.getTabCompleter(sender, args, index + 1));
            }
        }
        return completer;
    }

    /**
     * Get all subcommand that the sended commandsender can use
     * @param sender The commandsender
     * @return A list of the subcommands names
     */
    public List<String> getTabCompleter(CommandSender sender, String[] args, int index) {

        List<String> completer = new ArrayList<String>();
        if(!hasPermission(sender)) return completer;

        if (this.parameterMap.isEmpty()) return completer;

        if (this.parameterMap.isCommand()){
            completer = this.getSubCommandTabCompleter(completer,sender,args,index);
        }
        else {
            completer = this.getArgTabCompleter(completer,args,index);
        }
        return completer;

    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args){

        if(!this.hasPermission(sender)) return false;
        if(this.action == null){
            sender.sendMessage("§cThis command make nothing");
            return false;
        }

        try {
            this.action.apply(sender,label,args);
        }
        catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage("§cAn error occurred while trying to use this command, please contact administrator");
            return false;
        }
        return true;
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        // If there is no args or if command want arguments or if there is no subcommand matching with first args
        if(args.length == 0 || !this.parameterMap.isCommand()){
            return this.execute(sender, label, args);
        }
        if(!this.parameterMap.getAll().containsKey(args[0])){
            sender.sendMessage("§cThis command doesn't exist");
            return false;
        }

        SubCommand subCommand = (SubCommand) this.parameterMap.getAll().get(args[0]);

        int indexSubCommand = 1;

        for(int i = 1; i < args.length; i++){

            ParameterMap<Parameter> parameterMap1 = subCommand.parameterMap;

            if (parameterMap1.isCommand() && parameterMap1.getAll().containsKey(args[i])) {
                subCommand = (SubCommand) parameterMap1.getAll().get(args[i]);
                indexSubCommand++;
            }
            else {
                break;
            }
        }

        if(subCommand.parameterMap.isEmpty()){
            return subCommand.execute(sender, label, args);
        }

        // Copy real args into another list to remove subcommands for execution interface
        String[] cmdArgs = new String[args.length - indexSubCommand];
        System.arraycopy(args, indexSubCommand, cmdArgs,0,args.length - indexSubCommand);

        // Check if sender provide all args
        if(cmdArgs.length != subCommand.parameterMap.size()){
            if(subCommand.getUsage() != null) sender.sendMessage(subCommand.getUsage());
            return false;
        }
        else {
            subCommand.execute(sender, label, cmdArgs);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return this.getTabCompleter(sender,args,0);
    }

}
