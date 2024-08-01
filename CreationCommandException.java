package fr.zwartkat.zstaff.commands;

import fr.zwartkat.zstaff.commands.SubCommand;

public class CreationCommandException extends RuntimeException{

    private Object command;

    public CreationCommandException(Object command){
        if(command instanceof CommandGroup || command instanceof SubCommand){
            this.command = command;
        }

    }

    @Override
    public String getMessage(){
        if( command instanceof  SubCommand) {
            SubCommand subCommand = (SubCommand) command;
            if (!subCommand.getSubCommandArgs().isEmpty() && !subCommand.getSubCommandParameters().isEmpty()) {
                return "A SubCommand can't have arguments AND parameters, you can't give only arguments or parameters. Concerned subcommand :" + subCommand.getName();
            }
        }
        else if (command instanceof CommandGroup){
            CommandGroup command = (CommandGroup) this.command;
            if (!command.getCommandArgs().isEmpty() && !command.getCommandParameters().isEmpty()) {
                return "A Command can't have arguments AND parameters, you can't give only arguments or parameters. Concerned command :" + command.getName();
            }
        }

        return "A CreationCommandException has been create for an unknown reason (please contact the developer with the complete error to resolve it)";

    }
}
