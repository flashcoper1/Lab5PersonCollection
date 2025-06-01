package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Color;
import ru.ifmo.lab5.model.Person;

import java.util.TreeSet;

/**
 * Команда для вывода элементов, значение поля hairColor которых меньше заданного.
 */
public class FilterLessThanHairColorCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     * @param userInputHandler Обработчик пользовательского ввода.
     */
    public FilterLessThanHairColorCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public void execute(String arguments) {
        Color thresholdHairColor;
        if (arguments != null && !arguments.trim().isEmpty()) {
            String arg = arguments.trim();
            // "null" как аргумент не имеет смысла для "меньше чем", так как null не сравним по ординалу
            // Если пользователь введет "null", это будет ошибкой Enum.valueOf
            try {
                thresholdHairColor = Color.valueOf(arg.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: Некорректный эталонный цвет волос: " + arg + ". Пожалуйста, выберите из списка.");
                Color.printAllValues();
                return;
            }
        } else {
            // Запрашиваем у пользователя, null не разрешаем, так как "меньше чем null" не определено
            thresholdHairColor = userInputHandler.requestEnum(Color.class, "эталонный цвет волос для фильтрации (не может быть пустым)", false);
        }
        if (thresholdHairColor == null) { // Дополнительная проверка, если requestEnum как-то вернет null для non-nullable
            System.err.println("Ошибка: Эталонный цвет волос не может быть не указан для этой команды.");
            return;
        }


        TreeSet<Person> filteredPersons = collectionManager.filterLessThanHairColor(thresholdHairColor);

        if (filteredPersons.isEmpty()) {
            System.out.println("Не найдено людей с цветом волос меньше, чем " +
                    (thresholdHairColor != null ? thresholdHairColor.getRussianName() : "N/A") + ".");
        } else {
            System.out.println("Люди с цветом волос меньше, чем " +
                    (thresholdHairColor != null ? thresholdHairColor.getRussianName() : "N/A") + ":");
            for (Person person : filteredPersons) {
                System.out.println(person.toString());
                System.out.println("---");
            }
        }
    }

    @Override
    public String getDescription() {
        return "filter_less_than_hair_color hairColor : вывести элементы, значение поля hairColor которых меньше заданного (сравнение по порядку в Enum)";
    }
}