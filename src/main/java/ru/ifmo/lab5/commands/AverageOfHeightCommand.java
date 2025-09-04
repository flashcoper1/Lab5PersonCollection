package ru.ifmo.lab5.commands;
import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.util.CommandResult;

/**
 * Команда для нахождения среднего роста людей в коллекции.
 */
public class AverageOfHeightCommand implements Command {
    private final CollectionManager collectionManager;

    public AverageOfHeightCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResult execute(String arguments) {
        if (collectionManager.getCollection().isEmpty()) {
            return CommandResult.success("Коллекция пуста, невозможно рассчитать средний рост.");
        }
        double averageHeight = collectionManager.getAverageHeight();
        return CommandResult.success(String.format("Средний рост всех людей в коллекции: %.2f", averageHeight));
    }

    @Override
    public String getDescription() {
        return "average_of_height : вывести среднее значение поля height для всех элементов коллекции";
    }
}