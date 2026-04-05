package fr.zwartkat.commandsmaker.core.base.parameters;

public final class ParameterMapFactory {

    public static ParameterMap<Argument> argumentMap(){
        return new ArgumentParameterMap();
    }

    public static ParameterMap<SubCommand> subCommandMap(){
        return new SubCommandParameterMap();
    }
}
