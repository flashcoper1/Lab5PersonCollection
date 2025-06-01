package ru.ifmo.lab5.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

/**
 * Класс, представляющий координаты.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates {
    @XmlElement(required = true)
    private Double x; //Поле не может быть null
    @XmlElement
    private int y;

    /**
     * Конструктор по умолчанию для JAXB.
     */
    public Coordinates() {}

    /**
     * Создает новый объект Coordinates.
     * @param x Координата X. Не может быть null.
     * @param y Координата Y.
     */
    public Coordinates(Double x, int y) {
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
     * @param x Координата X. Не может быть null.
     * @throws IllegalArgumentException если x равен null.
     */
    public void setX(Double x) {
        if (x == null) {
            throw new IllegalArgumentException("Координата X не может быть null.");
        }
        this.x = x;
    }

    /**
     * Возвращает координату Y.
     * @return Координата Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Устанавливает координату Y.
     * @param y Координата Y.
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}