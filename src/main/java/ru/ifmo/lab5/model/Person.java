package ru.ifmo.lab5.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import ru.ifmo.lab5.util.ZonedDateTimeAdapter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Класс, представляющий человека. Хранится в коллекции.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Comparable<Person> {
    @XmlElement
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @XmlElement(required = true)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @XmlElement(required = true)
    private Coordinates coordinates; //Поле не может быть null

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @XmlElement(required = true)
    private Integer height; //Поле не может быть null, Значение поля должно быть больше 0

    @XmlElement
    private Color eyeColor; //Поле может быть null

    @XmlElement
    private Color hairColor; //Поле может быть null

    @XmlElement
    private Country nationality; //Поле может быть null

    @XmlElement(required = true)
    private Location location; //Поле не может быть null

    /**
     * Конструктор по умолчанию для JAXB.
     */
    public Person() {}

    /**
     * Создает новый объект Person. ID и дата создания должны быть установлены отдельно менеджером коллекции.
     * @param id Уникальный идентификатор.
     * @param name Имя. Не может быть null или пустым.
     * @param coordinates Координаты. Не могут быть null.
     * @param height Рост. Не может быть null, должен быть больше 0.
     * @param eyeColor Цвет глаз. Может быть null.
     * @param hairColor Цвет волос. Может быть null.
     * @param nationality Национальность. Может быть null.
     * @param location Местоположение. Не может быть null.
     */
    public Person(long id, String name, Coordinates coordinates, Integer height, Color eyeColor, Color hairColor, Country nationality, Location location) {
        // ID и creationDate устанавливаются в CollectionManager при добавлении или обновлении
        this.id = id;
        this.setName(name);
        this.setCoordinates(coordinates);
        this.setHeight(height);
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.setLocation(location);
        // creationDate будет установлено в CollectionManager
    }

    // Геттеры и сеттеры с валидацией

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id <= 0 && id != 0) { // 0 может быть временным значением перед установкой менеджером
            throw new IllegalArgumentException("ID должен быть больше 0.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым.");
        }
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null.");
        }
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("Дата создания не может быть null.");
        }
        this.creationDate = creationDate;
    }


    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        if (height == null || height <= 0) {
            throw new IllegalArgumentException("Рост не может быть null и должен быть больше 0.");
        }
        this.height = height;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Местоположение не может быть null.");
        }
        this.location = location;
    }

    @Override
    public int compareTo(Person other) {
        // Сортировка по умолчанию по ID
        return Long.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id; // Уникальность по ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person {" +
                "\n  id=" + id +
                ",\n  name='" + name + '\'' +
                ",\n  coordinates=" + coordinates +
                ",\n  creationDate=" + (creationDate != null ? creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss z")) : "N/A") +
                ",\n  height=" + height +
                ",\n  eyeColor=" + (eyeColor == null ? "N/A" : eyeColor.getRussianName()) +
                ",\n  hairColor=" + (hairColor == null ? "N/A" : hairColor.getRussianName()) +
                ",\n  nationality=" + (nationality == null ? "N/A" : nationality.getRussianName()) +
                ",\n  location=" + location +
                "\n}";
    }
}