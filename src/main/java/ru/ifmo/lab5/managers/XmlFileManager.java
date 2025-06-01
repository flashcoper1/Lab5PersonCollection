package ru.ifmo.lab5.managers;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import ru.ifmo.lab5.model.Coordinates;
import ru.ifmo.lab5.model.Location;
import ru.ifmo.lab5.model.Person;
import ru.ifmo.lab5.model.Color; // Импорт для JAXBContext
import ru.ifmo.lab5.model.Country; // Импорт для JAXBContext


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.TreeSet;

/**
 * Управляет загрузкой и сохранением коллекции в XML файл.
 */
public class XmlFileManager {

    private final String filePath;

    /**
     * Обертка для коллекции Person для корректной JAXB сериализации.
     */
    @XmlRootElement(name = "persons")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class PersonWrapper {
        @XmlElement(name = "person")
        private TreeSet<Person> persons = new TreeSet<>();

        public PersonWrapper(TreeSet<Person> persons) {
            this.persons = persons;
        }

        public TreeSet<Person> getPersons() {
            return persons;
        }
    }

    /**
     * Конструктор.
     * @param filePath Путь к файлу XML.
     */
    public XmlFileManager(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Загружает коллекцию из XML файла.
     * @return Загруженная коллекция или пустая, если файл не найден или произошла ошибка.
     */
    public TreeSet<Person> load() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Файл коллекции '" + filePath + "' не найден. Будет создана пустая коллекция.");
            return new TreeSet<>();
        }
        if (!file.canRead()) {
            System.err.println("Ошибка: нет прав на чтение файла '" + filePath + "'. Загрузка невозможна.");
            return new TreeSet<>();
        }

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)) {
            JAXBContext context = JAXBContext.newInstance(PersonWrapper.class, Person.class, Coordinates.class, Location.class, Color.class, Country.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            PersonWrapper wrapper = (PersonWrapper) unmarshaller.unmarshal(reader);
            TreeSet<Person> loadedPersons = (wrapper != null && wrapper.getPersons() != null) ? wrapper.getPersons() : new TreeSet<>();
            System.out.println("Коллекция успешно загружена из файла: " + filePath + ". Загружено элементов: " + loadedPersons.size());
            return loadedPersons;
        } catch (FileNotFoundException e) { // Эта ветка теперь менее вероятна из-за проверки file.exists()
            System.err.println("Файл коллекции не найден: " + filePath + ". Будет создана пустая коллекция.");
        } catch (JAXBException e) {
            System.err.println("Ошибка при чтении XML файла (возможно, файл поврежден или имеет неверный формат): " + e.getMessage());
            // e.printStackTrace(); // Для детальной отладки
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода при чтении файла: " + e.getMessage());
        }
        return new TreeSet<>();
    }

    /**
     * Сохраняет коллекцию в XML файл.
     * @param collection Коллекция для сохранения.
     */
    public void save(TreeSet<Person> collection) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            JAXBContext context = JAXBContext.newInstance(PersonWrapper.class, Person.class, Coordinates.class, Location.class, Color.class, Country.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            PersonWrapper wrapper = new PersonWrapper(collection);
            marshaller.marshal(wrapper, writer);
            System.out.println("Коллекция успешно сохранена в файл: " + filePath);
        } catch (JAXBException e) {
            System.err.println("Ошибка при записи XML файла: " + e.getMessage());
            // e.printStackTrace(); // Для детальной отладки
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода при записи файла: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Ошибка безопасности: нет прав на запись в файл '" + filePath + "'.");
        }
    }
}