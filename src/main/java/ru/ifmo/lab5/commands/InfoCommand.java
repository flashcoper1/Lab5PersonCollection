package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.util.CommandResult;

/**
 * Команда для вывода информации о коллекции.
 */
public class InfoCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды.
     * @param collectionManager Менеджер коллекции, информацию о которой нужно вывести.
     */
    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResult execute(String arguments) {
        return CommandResult.success(collectionManager.getInfo());
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}