package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Person;
import ru.ifmo.lab5.util.CommandResult;

/**
 * Команда для добавления нового элемента в коллекцию.
 * Запрашивает у пользователя все поля для создания объекта Person.
 */
public class AddCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор команды.
     * @param collectionManager Менеджер коллекции.
     * @param userInputHandler Обработчик пользовательского ввода.
     */
    public AddCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public CommandResult execute(String arguments) {
        try {
            Person newPerson = userInputHandler.requestPersonData();
            collectionManager.add(newPerson);
            return CommandResult.success("Новый человек успешно добавлен в коллекцию с ID: " + newPerson.getId());
        } catch (IllegalArgumentException e) {
            return CommandResult.error("Ошибка при добавлении: " + e.getMessage());
        } catch (Exception e) {
            // Ловим UserInterruptException и EndOfFileException, которые могут быть брошены из requestPersonData
            return CommandResult.error("Ввод данных был прерван. Команда не выполнена.");
        }
    }

    @Override
    public String getDescription() {
        return "add {element} : добавить новый элемент в коллекцию";
    }
}