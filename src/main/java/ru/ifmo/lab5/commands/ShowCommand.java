package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.model.Person;
import ru.ifmo.lab5.util.CommandResult;

/**
 * Команда для вывода всех элементов коллекции в строковом представлении.
 */
public class ShowCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды.
     * @param collectionManager Менеджер коллекции.
     */
    public ShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResult execute(String arguments) {
        if (collectionManager.getCollection().isEmpty()) {
            return CommandResult.success("Коллекция пуста.");
        }

        StringBuilder sb = new StringBuilder("Элементы коллекции:\n");
        for (Person person : collectionManager.getCollection()) {
            sb.append(person.toString()).append("\n");
            sb.append("---\n");
        }
        // Убираем последний разделитель "---"
        if (sb.length() > 4) {
            sb.setLength(sb.length() - 5);
        }
        return CommandResult.success(sb.toString());
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}