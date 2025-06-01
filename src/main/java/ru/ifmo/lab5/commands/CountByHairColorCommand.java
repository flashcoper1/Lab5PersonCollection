package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Color;

/**
 * Команда для вывода количества элементов, значение поля hairColor которых равно заданному.
 */
public class CountByHairColorCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     * @param userInputHandler Обработчик пользовательского ввода.
     */
    public CountByHairColorCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public void execute(String arguments) {
        Color hairColorToCount;
        if (arguments != null && !arguments.trim().isEmpty()) {
            String arg = arguments.trim();
            if (arg.equalsIgnoreCase("null")) {
                hairColorToCount = null;
            } else {
                try {
                    hairColorToCount = Color.valueOf(arg.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println("Ошибка: Некорректный цвет волос: " + arg + ". Пожалуйста, выберите из списка или введите 'null'.");
                    Color.printAllValues();
                    return;
                }
            }
        } else {
            hairColorToCount = userInputHandler.requestEnum(Color.class, "цвет волос для подсчета (можно оставить пустым для подсчета тех, у кого цвет не указан)", true);
        }

        long count = collectionManager.countByHairColor(hairColorToCount);
        System.out.println("Количество людей с цветом волос " +
                (hairColorToCount != null ? hairColorToCount.getRussianName() : "не указан") + ": " + count);
    }

    @Override
    public String getDescription() {
        return "count_by_hair_color [hairColor|null] : вывести количество элементов, значение поля hairColor которых равно заданному (или не указано, если ввести 'null')";
    }
}