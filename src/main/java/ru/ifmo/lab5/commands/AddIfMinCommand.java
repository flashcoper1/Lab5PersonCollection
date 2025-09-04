package ru.ifmo.lab5.commands;
import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Person;
import ru.ifmo.lab5.util.CommandResult;

/**
 * Команда для добавления нового человека если он меньше минимального.
 */
public class AddIfMinCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор команды.
     * @param collectionManager Менеджер коллекции для проверки и добавления.
     * @param userInputHandler Обработчик для запроса данных нового элемента.
     */
    public AddIfMinCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public CommandResult execute(String arguments) {
        try {
            Person newPerson = userInputHandler.requestPersonData();
            if (collectionManager.addIfMin(newPerson)) {
                return CommandResult.success("Элемент успешно добавлен с ID: " + newPerson.getId());
            } else {
                return CommandResult.success("Элемент не был добавлен, так как его значение (ID) не меньше минимального.");
            }
        } catch (Exception e) {
            return CommandResult.error("Ввод данных был прерван. Команда не выполнена.");
        }
    }

    @Override
    public String getDescription() {
        return "add_if_min {element} : добавить новый элемент, если его значение (ID) меньше минимального";
    }
}