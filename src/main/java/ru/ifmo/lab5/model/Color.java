package ru.ifmo.lab5.model;

/**
 * Перечисление возможных цветов.
 * Используется для цвета глаз и волос объекта Person.
 */
public enum Color {
    RED("Красный"),
    YELLOW("Желтый"),
    GREEN("Зеленый"),
    BLUE("Синий"),
    WHITE("Белый"),
    BROWN("Коричневый");

    private final String russianName;

    Color(String russianName) {
        this.russianName = russianName;
    }

    /**
     * Возвращает название цвета на русском языке.
     * @return Название цвета.
     */
    public String getRussianName() {
        return russianName;
    }

    /**
     * Выводит все доступные значения Enum с их русскими названиями.
     */
    public static void printAllValues() {
        System.out.println("Доступные цвета:");
        for (Color color : values()) {
            System.out.println(" - " + color.name() + " (" + color.getRussianName() + ")");
        }
    }
}