package ru.ifmo.lab5.managers;

import ru.ifmo.lab5.model.Person;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;
import java.util.Optional;

/**
 * Управляет коллекцией объектов Person.
 */
public class CollectionManager {
    private TreeSet<Person> collection = new TreeSet<>();
    private final ZonedDateTime initializationTime;
    private long nextId = 1;

    /**
     * Конструктор, инициализирует время создания коллекции.
     */
    public CollectionManager() {
        this.initializationTime = ZonedDateTime.now();
    }

    /**
     * Возвращает коллекцию.
     * @return Коллекция Person.
     */
    public TreeSet<Person> getCollection() {
        return collection;
    }

    /**
     * Устанавливает коллекцию, например, при загрузке из файла.
     * Также обновляет счетчик nextId.
     * @param loadedCollection Новая коллекция.
     */
    public void setCollection(TreeSet<Person> loadedCollection) {
        if (loadedCollection == null) {
            this.collection = new TreeSet<>();
        } else {
            this.collection = loadedCollection;
        }
        updateNextId();
    }

    private void updateNextId() {
        if (collection == null || collection.isEmpty()) {
            nextId = 1;
        } else {
            nextId = collection.stream().mapToLong(Person::getId).max().orElse(0L) + 1;
        }
    }

    /**
     * Добавляет новый элемент в коллекцию.
     * ID и дата создания генерируются автоматически.
     * @param person Новый человек для добавления (ID и дата создания будут перезаписаны).
     */
    public void add(Person person) {
        person.setId(nextId++);
        person.setCreationDate(LocalDateTime.now());
        collection.add(person);
    }

    /**
     * Добавляет элемент в коллекцию, если он больше максимального.
     * Сравнение происходит по естественному порядку (ID).
     * @param person Человек для добавления.
     * @return true, если элемент добавлен, иначе false.
     */
    public boolean addIfMax(Person person) {
        if (collection.isEmpty() || person.compareTo(collection.last()) > 0) {
            add(person);
            return true;
        }
        return false;
    }

    /**
     * Добавляет элемент в коллекцию, если он меньше минимального.
     * Сравнение происходит по естественному порядку (ID).
     * @param person Человек для добавления.
     * @return true, если элемент добавлен, иначе false.
     */
    public boolean addIfMin(Person person) {
        if (collection.isEmpty() || person.compareTo(collection.first()) < 0) {
            add(person);
            return true;
        }
        return false;
    }


    /**
     * Обновляет элемент коллекции с указанным ID.
     * @param id ID элемента для обновления.
     * @param updatedPersonData Объект Person с новыми данными (кроме ID и даты создания).
     * @return true, если элемент найден и обновлен, иначе false.
     */
    public boolean update(long id, Person updatedPersonData) {
        Optional<Person> personOptional = collection.stream().filter(p -> p.getId() == id).findFirst();
        if (personOptional.isPresent()) {
            Person personToUpdate = personOptional.get();
            LocalDateTime originalCreationDate = personToUpdate.getCreationDate();

            collection.remove(personToUpdate);

            updatedPersonData.setId(id);
            updatedPersonData.setCreationDate(originalCreationDate);
            collection.add(updatedPersonData);
            return true;
        }
        return false;
    }

    /**
     * Удаляет элемент из коллекции по ID.
     * @param id ID элемента для удаления.
     * @return true, если элемент найден и удален, иначе false.
     */
    public boolean removeById(long id) {
        return collection.removeIf(person -> person.getId() == id);
    }

    /**
     * Очищает коллекцию.
     */
    public void clear() {
        collection.clear();
        nextId = 1;
    }

    /**
     * Удаляет все элементы, которые больше указанного (сравнение по ID).
     * @param person Эталонный элемент (используется только его ID для сравнения).
     */
    public void removeGreater(Person person) {
        collection.removeIf(p -> p.compareTo(person) > 0);
    }

    /**
     * Удаляет все элементы, которые меньше указанного (сравнение по ID).
     * @param person Эталонный элемент (используется только его ID для сравнения).
     */
    public void removeLower(Person person) {
        collection.removeIf(p -> p.compareTo(person) < 0);
    }

    /**
     * Возвращает информацию о коллекции.
     * @return Строка с информацией.
     */
    public String getInfo() {
        return "Тип коллекции: " + collection.getClass().getName() +
                "\nДата инициализации: " + initializationTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss z")) +
                "\nКоличество элементов: " + collection.size();
    }

    /**
     * Возвращает средний рост всех людей в коллекции.
     * @return Средний рост или 0, если коллекция пуста.
     */
    public double getAverageHeight() {
        if (collection.isEmpty()) {
            return 0;
        }
        return collection.stream()
                .mapToLong(Person::getHeight)
                .average()
                .orElse(0.0);
    }

    /**
     * Подсчитывает количество людей с указанным цветом волос.
     * @param hairColorCriteria Цвет волос для подсчета (может быть null).
     * @return Количество людей.
     */
    public long countByHairColor(ru.ifmo.lab5.model.Color hairColorCriteria) {
        if (hairColorCriteria == null) {
            return collection.stream()
                    .filter(p -> p.getHairColor() == null)
                    .count();
        } else {
            return collection.stream()
                    .filter(p -> hairColorCriteria.equals(p.getHairColor()))
                    .count();
        }
    }

    /**
     * Фильтрует людей, у которых цвет волос "меньше" указанного.
     * Сравнение происходит по порядку объявления в Enum.
     * @param hairColor Эталонный цвет волос. Если null, возвращает пустой сет.
     * @return Отфильтрованный TreeSet.
     */
    public TreeSet<Person> filterLessThanHairColor(ru.ifmo.lab5.model.Color hairColor) {
        TreeSet<Person> filteredSet = new TreeSet<>();
        if (hairColor == null) return filteredSet;

        for (Person person : collection) {
            if (person.getHairColor() != null && person.getHairColor().ordinal() < hairColor.ordinal()) {
                filteredSet.add(person);
            }
        }
        return filteredSet;
    }
}