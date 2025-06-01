package ru.ifmo.lab5.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

/**
 * Класс, представляющий местоположение.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Location {
    @XmlElement(required = true)
    private Float x; //Поле не может быть null
    @XmlElement(required = true)
    private Double y; //Поле не может быть null
    @XmlElement
    private long z;
    @XmlElement
    private String name; //Длина строки не должна быть больше 400, Поле может быть null

    /**
     * Конструктор по умолчанию для JAXB.
     */
    public Location() {}

    /**
     * Создает новый объект Location.
     * @param x Координата X. Не может быть null.
     * @param y Координата Y. Не может быть null.
     * @param z Координата Z.
     * @param name Название местоположения. Может быть null, длина не более 400 символов.
     */
    public Location(Float x, Double y, long z, String name) {
        this.setX(x);
        this.setY(y);
        this.z = z;
        this.setName(name);
    }

    /**
     * Возвращает координату X.
     * @return Координата X.
     */
    public Float getX() {
        return x;
    }

    /**
     * Устанавливает координату X.
     * @param x Координата X. Не может быть null.
     * @throws IllegalArgumentException если x равен null.
     */
    public void setX(Float x) {
        if (x == null) {
            throw new IllegalArgumentException("Координата X местоположения не может быть null.");
        }
        this.x = x;
    }

    /**
     * Возвращает координату Y.
     * @return Координата Y.
     */
    public Double getY() {
        return y;
    }

    /**
     * Устанавливает координату Y.
     * @param y Координата Y. Не может быть null.
     * @throws IllegalArgumentException если y равен null.
     */
    public void setY(Double y) {
        if (y == null) {
            throw new IllegalArgumentException("Координата Y местоположения не может быть null.");
        }
        this.y = y;
    }

    /**
     * Возвращает координату Z.
     * @return Координата Z.
     */
    public long getZ() {
        return z;
    }

    /**
     * Устанавливает координату Z.
     * @param z Координата Z.
     */
    public void setZ(long z) {
        this.z = z;
    }

    /**
     * Возвращает название местоположения.
     * @return Название местоположения.
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название местоположения.
     * @param name Название местоположения. Длина не более 400 символов.
     * @throws IllegalArgumentException если длина имени превышает 400 символов.
     */
    public void setName(String name) {
        if (name != null && name.length() > 400) {
            throw new IllegalArgumentException("Длина имени местоположения не должна превышать 400 символов.");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + (name == null ? "N/A" : name) + '\'' +
                '}';
    }
}