package ru.ifmo.lab5.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

/**
 * Класс, представляющий 2D координаты.
 * Используется в классе {@link Person} для хранения местоположения.
 * Содержит валидацию для полей в соответствии с требованиями задания.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates {
    /**
     * Координата X. Поле не может быть null, максимальное значение: 348.
     */
    @XmlElement(required = true)
    private Double x;

    /**
     * Координата Y.
     */
    @XmlElement
    private float y;

    /**
     * Конструктор по умолчанию.
     * Необходим для корректной работы JAXB при десериализации XML.
     */
    public Coordinates() {}

    /**
     * Создает новый объект Coordinates с заданными значениями.
     * @param x Координата X.
     * @param y Координата Y.
     */
    public Coordinates(Double x, float y) {
        this.setX(x);
        this.y = y;
    }

    /**
     * Возвращает координату X.
     * @return Координата X.
     */
    public Double getX() {
        return x;
    }

    /**
     * Устанавливает координату X.
     * Проверяет, что значение не равно null и не превышает максимальное допустимое значение (348).
     * @param x Координата X для установки.
     * @throws IllegalArgumentException если x равен null или больше 348.
     */
    public void setX(Double x) {
        if (x == null) {
            throw new IllegalArgumentException("Координата X не может быть null.");
        }
        if (x > 348) {
            throw new IllegalArgumentException("Максимальное значение координаты X: 348.");
        }
        this.x = x;
    }

    /**
     * Возвращает координату Y.
     * @return Координата Y.
     */
    public float getY() {
        return y;
    }

    /**
     * Устанавливает координату Y.
     * @param y Координата Y для установки.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Возвращает строковое представление объекта Coordinates.
     * @return Строка в формате "Coordinates{x=..., y=...}".
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}