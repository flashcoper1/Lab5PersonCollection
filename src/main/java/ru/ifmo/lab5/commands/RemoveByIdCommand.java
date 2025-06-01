package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;

/**
 * Команда для удаления элемента из коллекции по ID.
 */
public class RemoveByIdCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveByIdCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String arguments) {
        if (arguments == null || arguments.trim().isEmpty()) {
            System.err.println("Ошибка: Необходимо указать ID элемента для удаления.");
            return;
        }
        try {
            long id = Long.parseLong(arguments.trim());
            if (collectionManager.removeById(id)) {
                System.out.println("Человек с ID " + id + " успешно удален.");
            } else {
                System.err.println("Ошибка: Человек с ID " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: ID должен быть числом.");
        }
    }

    @Override
    public String getDescription() {
        return "remove_by_id id : удалить элемент из коллекции по его id";
    }
}