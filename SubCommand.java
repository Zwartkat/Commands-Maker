package fr.zwartkat.commandsmaker;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.Supplier;

public class SubCommand implements SubCommandBase {

    /**
     * The name of the subcommand
     */
    private String subCommandName;
    /**
     * The permission who must require to see and use a subcommand
     */
    private String subCommandPermission;
    /**
     * The description of the subcommand
     */
    private String subCommandDescription;
    /**
     * The usage of the subcommand
     */
    private String subCommandUsage;
    /**
     * Action who must be execute by the subcommand
     */
    private SubCommandExecutionInterface<CommandSender, String, String[]> subCommandAction;
    /**
     * The argument(s) of the subcommand (if you set arguments you can't set parameters).
     * It is subcommands of the current subcommands
     */
    private HashMap<String, SubCommandBase> subCommands;
    /**
     * Args of the subcommand (if you set arguments you can't set subcommands)
     * It is data to give to the command (playername, number, etc...)
     * You can set multiple args but you must give args in the same order as the add of the args
     */
    private LinkedHashMap<String,List<String>> subArgs;

    public SubCommand(String name, String permission, String description){
        this.subCommandName = name;
        this.subCommandPermission = permission;
        this.subCommandDescription = description;
        this.subCommandUsage = null;
        this.subCommands = new HashMap<>();
        this.subArgs = new LinkedHashMap<>();
        this.subCommandAction = null;
    }

    /**
     * Get the name of the subcommand
     * @return The name of the subcommand
     */
    public String getName(){
        return this.subCommandName;
    }

    /**
     * Get the description of the subcommand
     * @return The description of the subcommand
     */
    public String getDescription(){
        return this.subCommandDescription;
    }

    /**
     * Get the permission of the subcommand
     * @return The permission of the subcommand
     */
    public String getPermission(){
        return this.subCommandPermission;
    }

    /**
     * Get the usage of the subcommand
     * @return The usage of the command
     */
    public String getUsage(){
        return this.subCommandUsage;
    }

    /**
     * Get arguments of the subcommand
     * @return The name and the instance of each args
     */
    public HashMap<String, SubCommandBase> getSubCommands(){
        return this.subCommands;
    }

    /**
     * Get parameters of the subcommand
     * @return The name and the type of each argument
     */
    public LinkedHashMap<String, List<String>> getSubCommandArguments(){
        return this.subArgs;
    }


    /**
     * To add a new subcommand to the subcommand
     * @param subCommand The instance of the subcommand
     */
    public void addSubCommand(SubCommandBase subCommand) throws CreationCommandException {
        this.subCommands.put(subCommand.getName(),subCommand);
    }

    /**
     * To add a parameter to the subcommand
     * @param name The name of the argument
     * @param tabCompleter List of arguments to show
     */
    public void addArg(String name, List<String> tabCompleter){this.subArgs.put(name,tabCompleter);}

    /**
     * Define the action of the subcommand
     * @param action The action to execute
     */
    public void setAction(SubCommandExecutionInterface<CommandSender, String, String[]> action){
        this.subCommandAction = action;
    }

    /**
     * Set the name of this subcommand
     * @param name The name to set on this subcommand
     * @return Instance of this subcommand
     */
    @Override
    public SubCommandBase setCommandName(String name){
        this.subCommandName = name;
        return this;
    }

    /**
     * Set the description of this subcommand
     * @param description The description to set on this subcommand
     * @return Instance of this subcommand
     */
    @Override
    public SubCommandBase setCommandDescription(String description) {
        this.subCommandDescription = description;
        return this;
    }

    /**
     * Set the usage message of this subcommand
     * @param message The usage message to set on this subcommand
     * @return Instance of this subcommand
     */
    @Override
    public SubCommandBase setCommandUsage(String message) {
        this.subCommandUsage = message;
        return this;
    }

    /**
     * Set the permission to use this subcommand
     * @param permission The permission to set on this subcommand
     * @return Instance of this subcommand
     */
    @Override
    public SubCommandBase setCommandPermission(String permission) {
        this.subCommandPermission = permission;
        return this;
    }

    /**
     * Get all subcommand that the sended commandsender can use
     * @param sender The commandsender
     * @return A list of the subcommands names
     */
    @Override
    public List<String> getTabCompleter(CommandSender sender, String[] args, Integer argIndex) {

        List<String> completer = new ArrayList<String>();

        if(sender.hasPermission(this.subCommandPermission) || sender.hasPermission("*")) {
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
            else if (!this.subCommands.isEmpty()) {
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

    /**
     * Execute the action of the subcommand
     * @param sender The sender of the command
     * @param label The command name
     * @param args The arguments of the command
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args){
        if(this.subCommandAction == null) {
            return false;
        }
        if(this.subCommandPermission.equals("*") || sender.hasPermission(this.subCommandPermission)){
            try{
                this.subCommandAction.apply(sender, label, args);
            }
            catch(Exception e){
                e.printStackTrace();
                sender.sendMessage("An error occurred while trying to use this command, please contact administrator");
                return false;
            }
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "You don't have the permission to execute this command");
            return false;
        }
    }
}
