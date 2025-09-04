package ru.ifmo.lab5.commands;
import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Person;
import ru.ifmo.lab5.util.CommandResult;

public class RemoveLowerCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    public RemoveLowerCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public CommandResult execute(String arguments) {
        if (collectionManager.getCollection().isEmpty()) {
            return CommandResult.success("Коллекция пуста, нечего удалять.");
        }
        try {
            long thresholdId = userInputHandler.requestPrimitiveLong("Введите ID эталонного элемента для сравнения:", false, 1L, null);
            Person tempThresholdPerson = new Person();
            tempThresholdPerson.setId(thresholdId);

            int initialSize = collectionManager.getCollection().size();
            collectionManager.removeLower(tempThresholdPerson);
            int removedCount = initialSize - collectionManager.getCollection().size();
            return CommandResult.success("Удалено " + removedCount + " элементов, ID которых меньше " + thresholdId + ".");
        } catch (Exception e) {
            return CommandResult.error("Ввод данных был прерван. Команда не выполнена.");
        }
    }

    @Override
    public String getDescription() {
        return "remove_lower : удалить из коллекции все элементы, ID которых меньше заданного";
    }
}