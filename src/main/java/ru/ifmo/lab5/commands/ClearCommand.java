package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;

/**
 * Команда для очистки коллекции.
 */
public class ClearCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     */
    public ClearCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String arguments) {
        collectionManager.clear();
        System.out.println("Коллекция успешно очищена.");
    }

    @Override
    public String getDescription() {
        return "clear : очистить коллекцию";
    }
}