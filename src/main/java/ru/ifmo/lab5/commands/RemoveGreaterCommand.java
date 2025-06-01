package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Person;

/**
 * Команда для удаления из коллекции всех элементов, превышающих заданный.
 */
public class RemoveGreaterCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     * @param userInputHandler Обработчик пользовательского ввода.
     */
    public RemoveGreaterCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public void execute(String arguments) {
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста, нечего удалять.");
            return;
        }
        try {
            long thresholdId = userInputHandler.requestPrimitiveLong("Введите ID эталонного элемента для сравнения (элементы с ID больше этого будут удалены):", false, 1L, null);
            Person tempThresholdPerson = new Person();
            tempThresholdPerson.setId(thresholdId);

            int initialSize = collectionManager.getCollection().size();
            collectionManager.removeGreater(tempThresholdPerson);
            int removedCount = initialSize - collectionManager.getCollection().size();
            System.out.println("Удалено " + removedCount + " элементов, ID которых больше " + thresholdId + ".");

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка при вводе данных для команды remove_greater: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "remove_greater : удалить из коллекции все элементы, ID которых больше ID заданного эталонного элемента";
    }
}