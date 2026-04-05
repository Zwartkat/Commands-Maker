package fr.zwartkat.commandsmaker.core.base.parameters;

public class ArgumentParameterMap extends ParameterMap<Argument>{
    @Override
    public Class<Argument> getType() {
        return Argument.class;
    }

    @Override
    public boolean isCommand() {
        return false;
    }
}
