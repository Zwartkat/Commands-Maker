package // Package

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class SubCommand {

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
    private SubCommandExecutionInterface subCommandAction;
    /**
     * The argument(s) of the subcommand (if you set arguments you can't set parameters).
     * It is subcommands of the current subcommands
     */
    private HashMap<String,SubCommand> subCommandArgs;
    /**
     * The parameter(s) of the subcommand (if you set arguments you can't set arguments)
     * It is data to give to the command (playername, number, etc...)
     * You can set multiple parameters but you must give parameters in the same order as the add of the parameters
     */
    private LinkedHashMap<String,List<String>> subCommandParameters;

    public SubCommand(String name, String permission, String description, String usage){
        this.subCommandName = name;
        this.subCommandPermission = permission;
        this.subCommandDescription = description;
        this.subCommandUsage = usage;
        this.subCommandArgs = new HashMap<>();
        this.subCommandParameters = new LinkedHashMap<>();
    }

    /**
     * Get the name of the subcommand
     * @return The name of the subcommand
     */
    public String getName(){
        return this.subCommandName;
    }

    /**
     * Get the permission of the subcommand
     * @return The permission of the subcommand
     */
    public String getSubCommandPermission(){
        return this.subCommandPermission;
    }

    /**
     * Get the usage of the subcommand
     * @return The usage of the command
     */
    public String getSubCommandUsage(){
        return this.subCommandUsage;
    }

    /**
     * Get arguments of the subcommand
     * @return The name and the instance of each args
     */
    public HashMap<String,SubCommand> getSubCommandArgs(){
        return this.subCommandArgs;
    }

    /**
     * Get parameters of the subcommand
     * @return The name and the type of each parameters
     */
    public LinkedHashMap<String, List<String>> getSubCommandParameters(){
        return this.subCommandParameters;
    }

    /**
     * Get all subcommand that the sended commandsender can use
     * @param sender The commandsender to check
     * @return A list of the subcommands names
     */
    public List<String> getAllowedSubCommand(CommandSender sender){
        List<String> allowedSubCommand = new ArrayList<>();
        if(sender instanceof Player){
            for(Map.Entry<String,SubCommand> subCommand : this.subCommandArgs.entrySet()){
                SubCommand subCommandObject = subCommand.getValue();
                if(subCommandObject.getSubCommandPermission().equals("")|| sender.hasPermission(subCommandObject.getSubCommandPermission())){
                    allowedSubCommand.add(subCommand.getKey());
                }
            }
        }

        return allowedSubCommand;
    }

    /**
     * To add a new subcommand to the subcommand
     * @param name The name of the subcommand
     * @param subCommand The instance of the subcommand
     */
    public void addArgument(String name, SubCommand subCommand) throws CreationCommandException {
        this.subCommandArgs.put(name,subCommand);
        if(!this.subCommandParameters.isEmpty()){
            throw new CreationCommandException(this);
        }
    }

    /**
     * To add a parameter to the subcommand
     * @param name The name of the parameter
     */
    public void addParameter(String name, List<String> tabCompleter){
        this.subCommandParameters.put(name,tabCompleter);
        if(!this.subCommandArgs.isEmpty()){
            throw new CreationCommandException(this);
        }

    }

    public void addParameter(String name){
        this.addParameter(name, List.of(""));
    }

    /**
     * Get all subcommands names of the current subcommand
     * @return A List of string with the names of all subcommands
     */
    public List<String> getSubCommandNames(){
        return this.subCommandArgs.keySet().stream().toList();
    }

    /**
     * Get the instance of the selected subcommand
     * @param subCommandName The name of the selected subcommand
     * @return The instance of the selected subcommand
     */
    public SubCommand getSubCommandInstance(String subCommandName){
        return this.subCommandArgs.get(subCommandName);
    }

    /**
     * Define the action of the subcommand
     * @param action The action to execute
     */
    public void setSubCommandAction(SubCommandExecutionInterface action){
        this.subCommandAction = action;
    }

    /**
     * Execute the action of the subcommand
     * @param commandSender The sender of the command
     * @param label The command name
     * @param args The arguments of the command
     */
    public void execute(CommandSender commandSender, String label, String[] args){
        if(this.subCommandPermission.equals("") || commandSender.hasPermission(this.subCommandPermission)){
            subCommandAction.apply(commandSender,label,args);
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "You don't have the permission to execute this command");
        }
    }

    public boolean equals(Object object){
        if(object instanceof SubCommand){
            SubCommand otherSubCommand = (SubCommand) object;
            if(this.subCommandName.equals(otherSubCommand.subCommandName) && this.subCommandPermission.equals(otherSubCommand.subCommandPermission) && this.subCommandArgs.equals(otherSubCommand.subCommandArgs) && this.subCommandParameters.equals(otherSubCommand.subCommandParameters) && this.subCommandAction.equals(otherSubCommand.subCommandAction) && this.subCommandUsage.equals(otherSubCommand.subCommandUsage)){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        String subCommandsStringList = new String();
        for(String subCommandName : this.getSubCommandNames()){
            subCommandsStringList += " " + subCommandName;
        }
        return new String("Nom:" + this.subCommandName + "\nPermission:" + this.subCommandPermission + "\nSous commandes: " + subCommandsStringList);
    }
}
