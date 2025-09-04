package ru.ifmo.lab5.commands;
import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.util.CommandResult;

public class ClearCommand implements Command {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResult execute(String arguments) {
        collectionManager.clear();
        return CommandResult.success("Коллекция успешно очищена.");
    }

    @Override
    public String getDescription() {
        return "clear : очистить коллекцию";
    }
}