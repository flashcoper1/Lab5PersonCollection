package ru.ifmo.lab5.managers;

import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import ru.ifmo.lab5.model.*;
import ru.ifmo.lab5.util.InputProvider;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Обрабатывает ввод данных пользователем из различных источников (консоль, файл).
 * Использует стек {@link InputProvider} для поддержки вложенных скриптов.
 */
public class UserInputHandler {
    private final Deque<InputProvider> providers = new ArrayDeque<>();

    /**
     * Добавляет нового поставщика ввода на вершину стека.
     * @param provider Поставщик ввода (например, из файла).
     */
    public void pushInputProvider(InputProvider provider) {
        providers.push(provider);
    }

    /**
     * Убирает поставщика ввода с вершины стека.
     */
    public void popInputProvider() {
        if (!providers.isEmpty()) {
            providers.pop();
        }
    }

    /**
     * Читает строку из текущего активного источника ввода.
     * @param prompt Приглашение для ввода (используется только для консоли).
     * @return Прочитанная строка.
     */
    private String readLine(String prompt) {
        if (providers.isEmpty()) {
            throw new IllegalStateException("Нет активного источника ввода.");
        }
        return providers.peek().readLine(prompt);
    }

    /**
     * Запрашивает у пользователя все данные для создания нового объекта Person.
     * @return Готовый к добавлению объект Person (с временным ID=0).
     */
    public Person requestPersonData() {
        System.out.println("Ввод данных для нового человека:");
        String name = requestString("Введите имя (не может быть пустым):", false);
        Coordinates coordinates = requestCoordinatesData();
        long height = requestPrimitiveLong("Введите рост (целое число > 0):", false, 1L, null);
        Color eyeColor = requestEnum(Color.class, "цвет глаз", true);
        Color hairColor = requestEnum(Color.class, "цвет волос", true);
        Country nationality = requestEnum(Country.class, "национальность", true);
        Location location = requestLocationData();
        return new Person(0, name, coordinates, height, eyeColor, hairColor, nationality, location);
    }

    private Coordinates requestCoordinatesData() {
        System.out.println("Ввод координат:");
        Double x = requestDouble("  Введите координату X (дробное число, не null, max: 348):", false, null, 348.0);
        float y = requestFloat("  Введите координату Y (дробное число):", false, null, null);
        return new Coordinates(x, y);
    }

    private Location requestLocationData() {
        System.out.println("Ввод местоположения:");
        Float x = requestFloat("  Введите координату X местоположения (дробное число, не null):", false, null, null);
        double y = requestDouble("  Введите координату Y местоположения (дробное число):", false, null, null);
        Double z = requestDouble("  Введите координату Z местоположения (дробное число, не null):", false, null, null);
        String name = requestString("  Введите название местоположения (до 400 символов, можно оставить пустым):", true, 400);
        return new Location(x, y, z, name);
    }

    public String requestString(String prompt, boolean nullable, Integer maxLength) {
        while (true) {
            try {
                String input = readLine(prompt);
                if (input == null) { // Может быть только от ConsoleInputProvider при Ctrl+D
                    if (nullable) return null;
                    System.err.println("Ошибка: Ввод обязательного поля был прерван. Повторите ввод.");
                    continue;
                }
                input = input.trim();
                if (input.isEmpty()) {
                    if (nullable) return null;
                    System.err.println("Ошибка: Это поле не может быть пустым.");
                    continue;
                }
                if (maxLength != null && input.length() > maxLength) {
                    System.err.println("Ошибка: Длина строки не должна превышать " + maxLength + " символов.");
                    continue;
                }
                return input;
            } catch (UserInterruptException e) {
                System.out.println("\n^C. Ввод прерван. Выход из программы...");
                System.exit(0);
            } catch (EndOfFileException e) {
                System.out.println("\nВвод завершен (EOF). Выход из программы...");
                System.exit(0);
            }
        }
    }

    public String requestString(String prompt, boolean nullable) {
        return requestString(prompt, nullable, null);
    }

    public <T extends Enum<T>> T requestEnum(Class<T> enumClass, String fieldNameForPrompt, boolean nullable) {
        System.out.println("Выберите " + fieldNameForPrompt + ":");
        if (enumClass == Color.class) Color.printAllValues();
        else if (enumClass == Country.class) Country.printAllValues();

        while (true) {
            try {
                String input = readLine("Введите название константы (или оставьте пустым, если разрешено null): ");
                if (input == null) {
                    if (nullable) return null;
                    System.err.println("Ошибка: Ввод обязательного поля был прерван. Повторите ввод.");
                    continue;
                }
                input = input.trim().toUpperCase();
                if (input.isEmpty()) {
                    if (nullable) return null;
                    System.err.println("Ошибка: Это поле не может быть пустым.");
                    continue;
                }
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: Некорректное значение. Пожалуйста, выберите из списка.");
            } catch (UserInterruptException | EndOfFileException e) {
                System.out.println("\nВвод прерван. Выход из программы...");
                System.exit(0);
            }
        }
    }

    // Универсальный метод для парсинга чисел
    private <T extends Number> T requestNumber(String prompt, boolean nullable, T min, T max, String typeName, java.util.function.Function<String, T> parser) {
        while (true) {
            try {
                String inputStr = readLine(prompt + " ");
                if (inputStr == null) {
                    if (nullable) return null;
                    System.err.println("Ошибка: Ввод обязательного поля был прерван. Повторите ввод.");
                    continue;
                }
                inputStr = inputStr.trim();
                if (inputStr.isEmpty()) {
                    if (nullable) return null;
                    System.err.println("Ошибка: Это поле не может быть пустым. Введите число.");
                    continue;
                }
                T value = parser.apply(inputStr.replace(',', '.'));
                if (min != null && value.doubleValue() < min.doubleValue()) {
                    System.err.println("Ошибка: Значение должно быть не меньше " + min + ".");
                    continue;
                }
                if (max != null && value.doubleValue() > max.doubleValue()) {
                    System.err.println("Ошибка: Значение должно быть не больше " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.err.println("Ошибка: Некорректный ввод. Пожалуйста, введите " + typeName + ".");
            } catch (UserInterruptException | EndOfFileException e) {
                System.out.println("\nВвод прерван. Выход из программы...");
                System.exit(0);
            }
        }
    }

    public float requestFloat(String prompt, boolean nullable, Float min, Float max) {
        Float result = requestNumber(prompt, nullable, min, max, "дробное число", Float::parseFloat);
        if (result == null) throw new IllegalStateException("Не удалось получить обязательное значение float.");
        return result;
    }

    public Double requestDouble(String prompt, boolean nullable, Double min, Double max) {
        return requestNumber(prompt, nullable, min, max, "дробное число", Double::parseDouble);
    }

    public long requestPrimitiveLong(String prompt, boolean nullable, Long min, Long max) {
        Long result = requestNumber(prompt, nullable, min, max, "целое число", Long::parseLong);
        if (result == null) throw new IllegalStateException("Не удалось получить обязательное значение long.");
        return result;
    }
}