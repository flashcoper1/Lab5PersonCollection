package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Person;

/**
 * Команда для добавления нового элемента в коллекцию.
 */
public class AddCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     * @param userInputHandler Обработчик пользовательского ввода.
     */
    public AddCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public void execute(String arguments) {
        try {
            Person newPerson = userInputHandler.requestPersonData();
            collectionManager.add(newPerson);
            System.out.println("Новый человек успешно добавлен в коллекцию с ID: " + newPerson.getId());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка при добавлении: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка при вводе данных для команды add: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "add {element} : добавить новый элемент в коллекцию";
    }
}