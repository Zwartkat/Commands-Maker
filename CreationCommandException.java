package fr.zwartkat.commandsmaker;

public class CreationCommandException extends RuntimeException{

    private Object command;

    public CreationCommandException(Object command){
        if(command instanceof CommandBase || command instanceof SubCommand){
            this.command = command;
        }

    }

    @Override
    public String getMessage(){
        if( command instanceof  SubCommand) {
            SubCommand subCommand = (SubCommand) command;
            if (!subCommand.getSubCommandArguments().isEmpty() && !subCommand.getSubCommandArguments().isEmpty()) {
                return "A SubCommand can't have arguments AND parameters, you can't give only arguments or parameters. Concerned subcommand :" + subCommand.getName();
            }
        }
        else if (command instanceof CommandBase){
            CommandBase command = (CommandBase) this.command;
            if (!command.getSubCommands().isEmpty() && !command.getSubArgs().isEmpty()) {
                return "A Executable can't have arguments AND parameters, you can't give only arguments or parameters. Concerned command :" + command.getName();
            }
        }

        return "A CreationCommandException has been create for an unknown reason (please contact the developer with the complete error to resolve it)";

    }
}
