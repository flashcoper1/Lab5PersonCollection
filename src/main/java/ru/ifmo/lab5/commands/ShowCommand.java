package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.model.Person;

/**
 * Команда для вывода всех элементов коллекции.
 */
public class ShowCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     */
    public ShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String arguments) {
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("Элементы коллекции:");
        for (Person person : collectionManager.getCollection()) {
            System.out.println(person.toString());
            System.out.println("---");
        }
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}