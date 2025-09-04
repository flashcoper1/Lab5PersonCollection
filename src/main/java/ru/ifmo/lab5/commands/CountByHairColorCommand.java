package ru.ifmo.lab5.commands;
import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Color;
import ru.ifmo.lab5.util.CommandResult;

/**
 * Команда для нахождения количества людей с определенным цветом волос.
 */
public class CountByHairColorCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор команды.
     * @param collectionManager Менеджер коллекции для выполнения подсчета.
     * @param userInputHandler Обработчик для запроса цвета, если он не указан в аргументах.
     */
    public CountByHairColorCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public CommandResult execute(String arguments) {
        Color hairColorToCount;
        try {
            if (arguments != null && !arguments.trim().isEmpty()) {
                String arg = arguments.trim();
                if (arg.equalsIgnoreCase("null")) {
                    hairColorToCount = null;
                } else {
                    hairColorToCount = Color.valueOf(arg.toUpperCase());
                }
            } else {
                hairColorToCount = userInputHandler.requestEnum(Color.class, "цвет волос для подсчета", true);
            }
        } catch (IllegalArgumentException e) {
            Color.printAllValues();
            return CommandResult.error("Некорректный цвет волос. Выберите из списка.");
        } catch (Exception e) {
            return CommandResult.error("Ввод был прерван.");
        }

        long count = collectionManager.countByHairColor(hairColorToCount);
        String colorName = (hairColorToCount != null) ? hairColorToCount.getRussianName() : "не указан";
        return CommandResult.success("Количество людей с цветом волос '" + colorName + "': " + count);
    }

    @Override
    public String getDescription() {
        return "count_by_hair_color [hairColor|null] : вывести количество элементов, значение поля hairColor которых равно заданному";
    }
}