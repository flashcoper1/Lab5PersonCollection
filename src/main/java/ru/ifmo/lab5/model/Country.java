package ru.ifmo.lab5.model;

/**
 * Перечисление возможных стран.
 * Используется для национальности объекта Person.
 */
public enum Country {
    INDIA("Индия"),
    VATICAN("Ватикан"),
    SOUTH_KOREA("Южная Корея");

    private final String russianName;

    Country(String russianName) {
        this.russianName = russianName;
    }

    /**
     * Возвращает название страны на русском языке.
     * @return Название страны.
     */
    public String getRussianName() {
        return russianName;
    }

    /**
     * Выводит все доступные значения Enum с их русскими названиями.
     */
    public static void printAllValues() {
        System.out.println("Доступные страны:");
        for (Country country : values()) {
            System.out.println(" - " + country.name() + " (" + country.getRussianName() + ")");
        }
    }
}