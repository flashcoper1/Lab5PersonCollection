package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Person;
import ru.ifmo.lab5.util.CommandResult;

/**
 * Команда для обновления элемента коллекции по его ID.
 * Запрашивает у пользователя новые данные для элемента.
 */
public class UpdateCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор команды.
     * @param collectionManager Менеджер коллекции.
     * @param userInputHandler Обработчик пользовательского ввода.
     */
    public UpdateCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public CommandResult execute(String arguments) {
        if (arguments == null || arguments.trim().isEmpty()) {
            return CommandResult.error("Необходимо указать ID элемента для обновления.");
        }
        try {
            long id = Long.parseLong(arguments.trim());
            if (collectionManager.getCollection().stream().noneMatch(p -> p.getId() == id)) {
                return CommandResult.error("Человек с ID " + id + " не найден.");
            }

            System.out.println("Ввод новых данных для человека с ID " + id + ":");
            Person updatedPersonData = userInputHandler.requestPersonData();

            if (collectionManager.update(id, updatedPersonData)) {
                return CommandResult.success("Человек с ID " + id + " успешно обновлен.");
            } else {
                return CommandResult.error("Не удалось обновить человека с ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            return CommandResult.error("ID должен быть числом.");
        } catch (Exception e) {
            return CommandResult.error("Ввод данных был прерван. Команда не выполнена.");
        }
    }

    @Override
    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}