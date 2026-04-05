package fr.zwartkat.commandsmaker.core.base.parameters;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Argument implements Parameter{

    private final String key;
    private final Supplier<List<String>> completer;

    public Argument(String key, List<String> completer){
        this.key = key;
        this.completer = () -> completer;
    }

    public Argument(String key, Supplier<List<String>> completer){
        this.key = key;
        this.completer = completer;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public boolean isExecutable() {
        return false;
    }

    public List<String> getValues(){
        return this.completer.get();
    }
}
