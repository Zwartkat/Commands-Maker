package fr.zwartkat.itemcreator.commands;

@FunctionalInterface
public interface SubCommandExecutionInterface<A,B,C,D> {
    void apply(A sender, B label, C args);
}
