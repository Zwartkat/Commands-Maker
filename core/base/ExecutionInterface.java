package fr.zwartkat.commandsmaker.core.base;

@FunctionalInterface
public interface ExecutionInterface<S,L,A> {
    void apply(S sender, L label, A args);
}
