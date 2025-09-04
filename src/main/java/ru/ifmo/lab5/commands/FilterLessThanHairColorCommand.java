package ru.ifmo.lab5.commands;
import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Color;
import ru.ifmo.lab5.model.Person;
import ru.ifmo.lab5.util.CommandResult;
import java.util.TreeSet;

/**
 * Команда для демонстрации персон с меньшим чем
 *                                  заданный цвет волос.
 */
public class FilterLessThanHairColorCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    public FilterLessThanHairColorCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public CommandResult execute(String arguments) {
        Color thresholdHairColor;
        try {
            if (arguments != null && !arguments.trim().isEmpty()) {
                thresholdHairColor = Color.valueOf(arguments.trim().toUpperCase());
            } else {
                thresholdHairColor = userInputHandler.requestEnum(Color.class, "эталонный цвет волос для фильтрации", false);
            }
        } catch (IllegalArgumentException e) {
            Color.printAllValues();
            return CommandResult.error("Некорректный цвет волос. Выберите из списка.");
        } catch (Exception e) {
            return CommandResult.error("Ввод был прерван.");
        }

        TreeSet<Person> filteredPersons = collectionManager.filterLessThanHairColor(thresholdHairColor);

        if (filteredPersons.isEmpty()) {
            return CommandResult.success("Не найдено людей с цветом волос меньше, чем " + thresholdHairColor.getRussianName() + ".");
        } else {
            StringBuilder sb = new StringBuilder("Люди с цветом волос меньше, чем " + thresholdHairColor.getRussianName() + ":\n");
            filteredPersons.forEach(person -> sb.append(person.toString()).append("\n---\n"));
            if (sb.length() > 4) sb.setLength(sb.length() - 5);
            return CommandResult.success(sb.toString());
        }
    }

    @Override
    public String getDescription() {
        return "filter_less_than_hair_color hairColor : вывести элементы, значение поля hairColor которых меньше заданного";
    }
}