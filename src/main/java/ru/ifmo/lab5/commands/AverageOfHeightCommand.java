package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;

/**
 * Команда для вывода среднего значения поля height для всех элементов коллекции.
 */
public class AverageOfHeightCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     */
    public AverageOfHeightCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String arguments) {
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста, невозможно рассчитать средний рост.");
            return;
        }
        double averageHeight = collectionManager.getAverageHeight();
        System.out.printf("Средний рост всех людей в коллекции: %.2f%n", averageHeight);
    }

    @Override
    public String getDescription() {
        return "average_of_height : вывести среднее значение поля height для всех элементов коллекции";
    }
}