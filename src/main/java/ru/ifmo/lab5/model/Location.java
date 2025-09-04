package ru.ifmo.lab5.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

/**
 * Класс, представляющий местоположение с 3D-координатами и названием.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Location {
    @XmlElement(required = true)
    private Float x; //Поле не может быть null

    @XmlElement
    private double y;

    @XmlElement(required = true)
    private Double z; //Поле не может быть null

    @XmlElement
    private String name; //Длина строки не должна быть больше 400, Поле может быть null

    /**
     * Конструктор по умолчанию для JAXB.
     */
    public Location() {}

    /**
     * Создает новый объект Location.
     * @param x Координата X. Не может быть null.
     * @param y Координата Y.
     * @param z Координата Z. Не может быть null.
     * @param name Название местоположения.
     */
    public Location(Float x, double y, Double z, String name) {
        this.setX(x);
        this.y = y;
        this.setZ(z);
        this.setName(name);
    }

    public Float getX() { return x; }
    public void setX(Float x) {
        if (x == null) throw new IllegalArgumentException("Координата X местоположения не может быть null.");
        this.x = x;
    }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public Double getZ() { return z; }
    public void setZ(Double z) {
        if (z == null) throw new IllegalArgumentException("Координата Z местоположения не может быть null.");
        this.z = z;
    }
    public String getName() { return name; }
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