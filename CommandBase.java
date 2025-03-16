package fr.zwartkat.commandsmaker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;

public class CommandBase extends Command implements CommandExecutor, TabExecutor, ExecutableCommand {

    private String name,description,permission,usageMessage;
    private List<String> aliases;
    private HashMap<String,SubCommandBase> subCommands;
    private LinkedHashMap<String,List<String>> subArgs;
    private  SubCommandExecutionInterface<CommandSender,String, String[]> action;

    public CommandBase(String name, String description, String permission, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.usageMessage = usageMessage;
        this.aliases = aliases;
        this.subCommands = new HashMap<>();
        this.subArgs = new LinkedHashMap<>();
        this.action = null;

    }

    /**
     * Get subcommands of this command
     * @return
     */
    public HashMap<String,SubCommandBase> getSubCommands() {
        return subCommands;
    }

    /**
     * Get arguments of this command
     * @return
     */
    public LinkedHashMap<String,List<String>> getSubArgs() {
        return subArgs;
    }

    /**
     * Add a subcommand on this command
     * @param subCommand the subcommand to add
     */
    @Override
    public void addSubCommand(SubCommandBase subCommand){
        this.subCommands.put(subCommand.getName(),subCommand);
    }

    /**
     * Add an argument on this command
     * @param argKey The key of the argument
     * @param tabCompleterArgs The list of arguments
     */
    @Override
    public void addArg(String argKey, List<String> tabCompleterArgs){
        this.subArgs.put(argKey, tabCompleterArgs);
    }

    /**
     * Define the action of the subcommand
     * @param action The action to execute
     */
    public void setAction(SubCommandExecutionInterface<CommandSender, String, String[]> action){
        this.action = action;
    }

    /**
     *
     * @param sender The sender of the command
     * @param args Args of the command
     * @param argIndex The index of the current argument
     * @return A list of string for the completer
     */
    @Override
    public List<String> getTabCompleter(CommandSender sender, String[] args, Integer argIndex) {

        List<String> completer = new ArrayList<String>();

        if(sender.hasPermission(this.permission) || sender.hasPermission("*")) {
            // If there are arguments
            if (!this.subArgs.isEmpty()) {

                if ((args.length - argIndex) <= this.subArgs.size()) {
                    Iterator<Map.Entry<String, List<String>>> iterator = this.subArgs.entrySet().iterator();
                    int currentIndex = 1;
                    while (iterator.hasNext()) {
                        Map.Entry<String, List<String>> entry = iterator.next();

                        if (currentIndex == args.length - argIndex) {
                            completer.addAll(entry.getValue());
                            break;
                        }
                        currentIndex++;
                    }
                }
            }
            // If there are subcommands
            if (!this.subCommands.isEmpty()) {
                // If the current argument is the first
                if (args.length == argIndex + 1) {

                    this.subCommands.forEach((name, subCommand) -> {
                        if (sender.hasPermission(subCommand.getPermission()) || sender.hasPermission("*")) {
                            completer.add(name);
                        }
                    });
                }
                // Else take the completer of used subcommands
                else {

                    if (this.subCommands.keySet().contains(args[argIndex])) {
                        completer.addAll(this.subCommands.get(args[argIndex]).getTabCompleter(sender, args, argIndex + 1));
                    }
                }
            }
        }

        return completer;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args){

        if(this.action == null) return false;

        try {
            this.action.apply(sender,label,args);
        }
        catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage("An error occurred while trying to use this command, please contact administrator");
            return false;
        }
        return true;
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 || !this.subCommands.containsKey(args[0])){
            this.execute(sender, label, args);
        }
        else {
            if(!this.subCommands.containsKey(args[0])){
                sender.sendMessage("This command doesn't exist");
                return false;
            }

            SubCommandBase subCommand = this.subCommands.get(args[0]);

            int nbSubCommand = 1;

            for(int i = 1; i < args.length; i++){
                if(!subCommand.getSubCommands().containsKey(args[i])) break;

                subCommand = subCommand.getSubCommands().get(args[i]);
                nbSubCommand++;
            }

            if(subCommand.getSubCommandArguments().isEmpty()) return subCommand.execute(sender, label, args);

            String[] argsList = new String[args.length - nbSubCommand];

            if(!subCommand.getSubCommandArguments().isEmpty()){
                System.arraycopy(args, nbSubCommand, argsList,0,args.length - nbSubCommand);
            }

            if(argsList.length != subCommand.getSubCommandArguments().size()){
                if(subCommand.getUsage() != null) sender.sendMessage(subCommand.getUsage());
                return false;
            }
            else {
                subCommand.execute(sender, label, argsList);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> completer = this.getTabCompleter(sender,args,0);

        return completer;
    }

}
