package fr.zwartkat.commandsmaker.core.base.parameters;

import fr.zwartkat.commandsmaker.core.base.parameters.Parameter;
import java.util.LinkedHashMap;

public abstract class ParameterMap<T extends Parameter> {

    private final LinkedHashMap<String,T> parameters = new LinkedHashMap<>();

    public void add(T param){
        parameters.put(param.getKey(),param);
    }

    public int size(){
        return parameters.size();
    }

    public abstract Class<T> getType();
    public abstract boolean isCommand();

    public boolean isEmpty(){
        return parameters.isEmpty();
    }

    public LinkedHashMap<String,T> getAll(){
        return parameters;
    }
}
