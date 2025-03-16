package fr.zwartkat.commandsmaker;

@FunctionalInterface
public interface SubCommandExecutionInterface<S,L,A> {
    void apply(S sender, L label, A args);
}
