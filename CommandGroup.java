package // Package name

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class CommandGroup extends Command implements CommandExecutor, TabExecutor {

    private String commandName;
    private String commandDescription;
    private String commandUsage;
    private List<String> commandAliases;
    private HashMap<String, SubCommand> subCommands;
    private LinkedHashMap<String, List<String>> parameters;

    protected CommandGroup(String name, String description, String usage, List<String> aliases) {
        super(name,description,usage,aliases);
        this.commandName = name;
        this.commandDescription = description;
        this.commandUsage = usage;
        this.commandAliases = aliases;
        this.subCommands = new HashMap<>();
        this.parameters = new LinkedHashMap<>();
    }

    public String getCommandName(){
        return this.commandName;
    }
    public String getCommandDescription(){
        return this.commandDescription;
    }

    public String getCommandUsage(){
        return this.commandUsage;
    }

    public List<String> getCommandAliases(){
        return this.commandAliases;
    }

    public HashMap<String, SubCommand> getCommandArgs(){
        return this.subCommands;
    }

    /**
     * Get parameters of the subcommand
     * @return The name and the type of each parameters
     */
    public LinkedHashMap<String, List<String>> getCommandParameters(){
        return this.parameters;
    }


    public void addSubCommand(String subCommandName, SubCommand subCommand){
        this.subCommands.put(subCommandName,subCommand);
        if(!this.parameters.isEmpty()){
            throw new CreationCommandException(this);
        }
    }

    public void addParameter(String name, List<String> parameterCompleter){
        this.parameters.put(name,parameterCompleter);
        if(!this.subCommands.isEmpty()){
            throw new CreationCommandException(this);
        }
    }

    public void addParameter(String name){
        this.addParameter(name, List.of(""));
    }


    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        SubCommand subCommand;

        if(subCommands.keySet().contains(args[0])){
            subCommand = this.subCommands.get(args[0]);
            int nbSubCommand = 1;
            for(int i = 1; i <= args.length; i++){
                if(i == args.length){
                    String[] argsList = new String[args.length - nbSubCommand];;
                    if(!subCommand.getSubCommandParameters().isEmpty()){
                        System.arraycopy(args, nbSubCommand, argsList,0,args.length - nbSubCommand);
                    }
                    if(argsList.length != subCommand.getSubCommandParameters().size()){
                        sender.sendMessage(subCommand.getSubCommandUsage());
                    }
                    else {
                        subCommand.execute(sender,commandLabel,argsList);
                    }

                    return true;
                }
                else if(subCommand.getSubCommandNames().contains(args[i])){
                    subCommand = subCommand.getSubCommandInstance(args[i]);
                    nbSubCommand++;
                }
                else if(subCommand.getSubCommandParameters().isEmpty()){
                    sender.sendMessage(ChatColor.RED + "Argument error");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command == null){
            ZStaff.plugin.getLogger().warning("The command "+ label + " doesn't exist or couldn't be loaded, check loading plugin errors");
            sender.sendMessage(ChatColor.RED + "The command doesn't exist or couldn't be loaded, check the console");
            return true;
        }
        else {
            return execute(sender, label, args);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completer = new ArrayList<>();

        if(!this.parameters.isEmpty()){
            if(args.length <= this.parameters.size()){
                List<String> parameterNames = new ArrayList<>(this.parameters.keySet());
                sender.sendMessage(parameterNames.get(args.length-1));
                completer = this.getPredefinedCompleter(parameterNames.get(args.length - 1));
            }
        }
        else {
            if (args.length == 1) {
                completer.addAll(this.subCommands.keySet());
                return completer;
            }
            if (this.subCommands.containsKey(args[0])) {
                List<String> targetParameterCompleter, parameterName = new ArrayList<>();
                String targetParameter = "";
                int nbSubCommand = 1;
                SubCommand subCommand = this.subCommands.get(args[0]);
                for (int i = 1; i < args.length; i++) {
                    parameterName = new ArrayList<>(subCommand.getSubCommandParameters().keySet());
                    if (i == args.length - 1) {
                        if (!subCommand.getSubCommandParameters().isEmpty() && sender.hasPermission(subCommand.getSubCommandPermission())) {
                            targetParameter = parameterName.get(i - nbSubCommand);
                            targetParameterCompleter = subCommand.getSubCommandParameters().get(targetParameter);
                            completer = this.getPredefinedCompleter(targetParameter);
                            if (completer == null && !targetParameterCompleter.isEmpty()) {
                                completer = targetParameterCompleter;
                            } else if (targetParameterCompleter.isEmpty()) {
                                completer = Arrays.asList(new String("<" + targetParameter + ">"));
                            }
                            return this.filterParameters(completer, args[nbSubCommand + i - nbSubCommand]);

                        } else {
                            return subCommand.getAllowedSubCommand(sender);
                        }

                    } else if (subCommand.getSubCommandNames().contains(args[i])) {
                        subCommand = subCommand.getSubCommandInstance(args[i]);
                        nbSubCommand++;
                    } else if (args.length - 2 >= nbSubCommand && parameterName.size() >= (args.length - nbSubCommand)) {
                        for (int j = 0; j > (args.length - 1 - nbSubCommand); i++) {
                            targetParameter = parameterName.get(j);
                        }
                    } else {
                        return new ArrayList<>();
                    }
                }
            }
        }
        if(completer != null){
            for (String comp : completer){
                sender.sendMessage(comp);
            }
        }


        return completer;
    }

    private List<String> filterParameters(List<String> initialCompleter, String parameter){
        List<String> completer = new ArrayList<>();
        if(parameter.equals("")){
            return initialCompleter;
        }
        for(String arg : initialCompleter){
            if(arg.toUpperCase().startsWith(parameter.toUpperCase())){
                completer.add(arg);
            }
        }
        return completer;
    }

    private List<String> getPredefinedCompleter(String parameterName){
        switch (parameterName){
            case "player":
                return this.getPlayersNames();
            case "items":
                return this.getItems();
            case "world":
                return this.getWorlds();
            case "integer":
                return this.getIntExemple();
            default:
                return this.parameters.get(parameterName);
        }
    }

    private List<String> getPlayersNames(){
        List<String> playersNames = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            playersNames.add(player.getName());
        }
        return playersNames;
    }

    private List<String> getWorlds(){
        List<String> worldNames = new ArrayList<>();
        for(World world : Bukkit.getWorlds()){
            worldNames.add(world.getName());
        }
        return worldNames;
    }

    private List<String> getItems(){
        List<String> itemNames = new ArrayList<>();
        for(Material item : Material.values()){
            if(item.isItem()){
                itemNames.add(item.name());
            }
        }
        return itemNames;
    }

    private List<String> getIntExemple(){
        List<String> intList = new ArrayList<>();
        intList.addAll(Arrays.asList("1","10","100","1000"));
        return intList;
    }
}
