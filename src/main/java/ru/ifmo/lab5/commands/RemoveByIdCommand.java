package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.util.CommandResult;

/**
 * Команда для удаления элемента из коллекции по его ID.
 */
public class RemoveByIdCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveByIdCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResult execute(String arguments) {
        if (arguments == null || arguments.trim().isEmpty()) {
            return CommandResult.error("Необходимо указать ID элемента для удаления.");
        }
        try {
            long id = Long.parseLong(arguments.trim());
            if (collectionManager.removeById(id)) {
                return CommandResult.success("Человек с ID " + id + " успешно удален.");
            } else {
                return CommandResult.error("Человек с ID " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            return CommandResult.error("ID должен быть числом.");
        }
    }

    @Override
    public String getDescription() {
        return "remove_by_id id : удалить элемент из коллекции по его id";
    }
}