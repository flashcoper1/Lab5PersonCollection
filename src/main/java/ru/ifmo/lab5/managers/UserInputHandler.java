package ru.ifmo.lab5.managers;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import ru.ifmo.lab5.model.*;

// Scanner больше не нужен
// import java.util.Scanner;

/**
 * Обрабатывает ввод данных пользователем с использованием JLine.
 */
public class UserInputHandler {
    private final LineReader lineReader;

    /**
     * Конструктор.
     * @param lineReader Настроенный LineReader.
     */
    public UserInputHandler(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    /**
     * Читает строку с помощью LineReader.
     * @param prompt Приглашение для ввода.
     * @return Введенная строка.
     * @throws EndOfFileException если ввод был прерван (Ctrl+D).
     * @throws UserInterruptException если ввод был прерван (Ctrl+C).
     */
    private String readLineWithJLine(String prompt) throws EndOfFileException, UserInterruptException {
        return lineReader.readLine(prompt);
    }

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
            String input;
            try {
                input = readLineWithJLine(prompt + " ");
                if (input == null && !nullable) { // Ctrl+D на обязательном поле
                    System.err.println("Ошибка: Ввод обязательного поля был прерван (Ctrl+D). Повторите ввод.");
                    continue;
                } else if (input == null && nullable) { // Ctrl+D на необязательном поле
                    return null;
                }
                input = input.trim();
            } catch (UserInterruptException e) { // Ctrl+C
                System.out.println("^C"); // Эмуляция поведения терминала
                System.err.println("Ошибка: Ввод был прерван (Ctrl+C). Повторите ввод.");
                continue;
            }  catch (EndOfFileException e) { // Должно быть поймано выше, но для полноты
                if (nullable) return null;
                System.err.println("Ошибка: Ввод обязательного поля был прерван (EOF). Повторите ввод.");
                continue;
            }


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
        }
    }

    public String requestString(String prompt, boolean nullable) {
        return requestString(prompt, nullable, null);
    }

    public Integer requestInteger(String prompt, boolean nullable, Integer minValue, Integer maxValue) {
        while (true) {
            String inputStr = null;
            try {
                inputStr = readLineWithJLine(prompt + " ");
                if (inputStr == null) {
                    if (nullable) {
                        return null;
                    } else {
                        System.err.println("Ошибка: Ввод обязательного поля был прерван. Повторите ввод.");
                        continue;
                    }
                }
                inputStr = inputStr.trim();
            } catch (UserInterruptException e) {
                System.out.println("^C");
                System.err.println("Ошибка: Ввод был прерван (Ctrl+C). Повторите ввод.");
                continue;
            } catch (EndOfFileException e) {
                if (nullable) return null;
                System.err.println("Ошибка: Ввод обязательного поля был прерван (EOF). Повторите ввод.");
                continue;
            }


            if (inputStr.isEmpty()) {
                if (nullable) return null;
                System.err.println("Ошибка: Это поле не может быть пустым. Введите число.");
                continue;
            }
            try {
                int value = Integer.parseInt(inputStr);
                if (minValue != null && value < minValue) {
                    System.err.println("Ошибка: Значение должно быть не меньше " + minValue + ".");
                    continue;
                }
                if (maxValue != null && value > maxValue) {
                    System.err.println("Ошибка: Значение должно быть не больше " + maxValue + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.err.println("Ошибка: Некорректный ввод. Пожалуйста, введите целое число.");
            }
        }
    }

    public int requestInt(String prompt, boolean nullable, Integer minValue, Integer maxValue) {
        Integer result = requestInteger(prompt, nullable, minValue, maxValue);
        if (result == null) { // Это может произойти только если nullable=true и был введен null/EOF/Ctrl+C
            // Если поле действительно не может быть null по логике, то nullable должно быть false
            // и этот блок не должен достигаться, так как requestInteger будет повторять запрос.
            // Для примитива int, если он обязателен, мы должны получить значение.
            throw new IllegalStateException("Не удалось получить обязательное значение int (возможно, ввод был прерван).");
        }
        return result;
    }

    // Аналогичные изменения для requestDouble, requestFloat, requestLong, requestPrimitiveLong, requestEnum
    // с обработкой UserInterruptException и EndOfFileException от readLineWithJLine

    public Double requestDouble(String prompt, boolean nullable, Double minValue, Double maxValue) {
        while (true) {
            String inputStr = null;
            try {
                inputStr = readLineWithJLine(prompt + " ");
                if (inputStr == null && !nullable) { System.err.println("Ошибка: Ввод обязательного поля был прерван (Ctrl+D). Повторите ввод."); continue; }
                else if (inputStr == null && nullable) { return null; }
                inputStr = inputStr.trim();
            } catch (UserInterruptException e) { System.out.println("^C"); System.err.println("Ошибка: Ввод был прерван (Ctrl+C). Повторите ввод."); continue; }
            catch (EndOfFileException e) { if (nullable) return null; System.err.println("Ошибка: Ввод обязательного поля был прерван (EOF). Повторите ввод."); continue; }

            if (inputStr.isEmpty()) {
                if (nullable) return null;
                System.err.println("Ошибка: Это поле не может быть пустым. Введите число."); continue;
            }
            try {
                double value = Double.parseDouble(inputStr.replace(',', '.'));
                if (minValue != null && value < minValue) { System.err.println("Ошибка: Значение должно быть не меньше " + minValue + "."); continue; }
                if (maxValue != null && value > maxValue) { System.err.println("Ошибка: Значение должно быть не больше " + maxValue + "."); continue; }
                return value;
            } catch (NumberFormatException e) { System.err.println("Ошибка: Некорректный ввод. Пожалуйста, введите дробное число (например, 3.14)."); }
        }
    }

    public Float requestFloat(String prompt, boolean nullable, Float minValue, Float maxValue) {
        while (true) {
            String inputStr = null;
            try {
                inputStr = readLineWithJLine(prompt + " ");
                if (inputStr == null && !nullable) { System.err.println("Ошибка: Ввод обязательного поля был прерван (Ctrl+D). Повторите ввод."); continue; }
                else if (inputStr == null && nullable) { return null; }
                inputStr = inputStr.trim();
            } catch (UserInterruptException e) { System.out.println("^C"); System.err.println("Ошибка: Ввод был прерван (Ctrl+C). Повторите ввод."); continue; }
            catch (EndOfFileException e) { if (nullable) return null; System.err.println("Ошибка: Ввод обязательного поля был прерван (EOF). Повторите ввод."); continue; }

            if (inputStr.isEmpty()) {
                if (nullable) return null;
                System.err.println("Ошибка: Это поле не может быть пустым. Введите число."); continue;
            }
            try {
                float value = Float.parseFloat(inputStr.replace(',', '.'));
                if (minValue != null && value < minValue) { System.err.println("Ошибка: Значение должно быть не меньше " + minValue + "."); continue; }
                if (maxValue != null && value > maxValue) { System.err.println("Ошибка: Значение должно быть не больше " + maxValue + "."); continue; }
                return value;
            } catch (NumberFormatException e) { System.err.println("Ошибка: Некорректный ввод. Пожалуйста, введите дробное число (например, 3.14).");}
        }
    }

    public Long requestLong(String prompt, boolean nullable, Long minValue, Long maxValue) {
        while (true) {
            String inputStr = null;
            try {
                inputStr = readLineWithJLine(prompt + " ");
                if (inputStr == null && !nullable) { System.err.println("Ошибка: Ввод обязательного поля был прерван (Ctrl+D). Повторите ввод."); continue; }
                else if (inputStr == null && nullable) { return null; }
                inputStr = inputStr.trim();
            } catch (UserInterruptException e) { System.out.println("^C"); System.err.println("Ошибка: Ввод был прерван (Ctrl+C). Повторите ввод."); continue; }
            catch (EndOfFileException e) { if (nullable) return null; System.err.println("Ошибка: Ввод обязательного поля был прерван (EOF). Повторите ввод."); continue; }

            if (inputStr.isEmpty()) {
                if (nullable) return null;
                System.err.println("Ошибка: Это поле не может быть пустым. Введите число."); continue;
            }
            try {
                long value = Long.parseLong(inputStr);
                if (minValue != null && value < minValue) { System.err.println("Ошибка: Значение должно быть не меньше " + minValue + "."); continue; }
                if (maxValue != null && value > maxValue) { System.err.println("Ошибка: Значение должно быть не больше " + maxValue + "."); continue; }
                return value;
            } catch (NumberFormatException e) { System.err.println("Ошибка: Некорректный ввод. Пожалуйста, введите целое число.");}
        }
    }

    public long requestPrimitiveLong(String prompt, boolean nullable, Long minValue, Long maxValue) {
        Long result = requestLong(prompt, nullable, minValue, maxValue);
        if (result == null) { // Должно быть обработано в requestLong для non-nullable
            throw new IllegalStateException("Не удалось получить обязательное значение long (возможно, ввод был прерван).");
        }
        return result;
    }

    public <T extends Enum<T>> T requestEnum(Class<T> enumClass, String fieldNameForPrompt, boolean nullable) {
        System.out.println("Выберите " + fieldNameForPrompt + ":");
        if (enumClass == Color.class) Color.printAllValues();
        else if (enumClass == Country.class) Country.printAllValues();

        while (true) {
            String input = null;
            try {
                input = readLineWithJLine("Введите название константы (или оставьте пустым, если разрешено null): ");
                if (input == null && !nullable) { System.err.println("Ошибка: Ввод обязательного поля Enum был прерван (Ctrl+D). Повторите ввод."); continue; }
                else if (input == null && nullable) { return null; }
                input = input.trim().toUpperCase();
            } catch (UserInterruptException e) { System.out.println("^C"); System.err.println("Ошибка: Ввод был прерван (Ctrl+C). Повторите ввод."); continue; }
            catch (EndOfFileException e) { if (nullable) return null; System.err.println("Ошибка: Ввод обязательного поля Enum был прерван (EOF). Повторите ввод."); continue; }

            if (input.isEmpty()) {
                if (nullable) return null;
                System.err.println("Ошибка: Это поле не может быть пустым.");
                continue;
            }
            try {
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: Некорректное значение. Пожалуйста, выберите из списка.");
            }
        }
    }
}