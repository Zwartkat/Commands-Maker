package fr.zwartkat.commandsmaker.core.base.parameters;

public class SubCommandParameterMap extends ParameterMap<SubCommand>{

    @Override
    public Class<SubCommand> getType() {
        return SubCommand.class;
    }

    @Override
    public boolean isCommand() {
        return true;
    }
}
