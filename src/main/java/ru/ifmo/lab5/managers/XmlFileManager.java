package ru.ifmo.lab5.managers;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import ru.ifmo.lab5.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.TreeSet;

/**
 * Управляет загрузкой и сохранением коллекции в XML файл.
 * Этот класс не взаимодействует с консолью, а сообщает об ошибках через исключения.
 */
public class XmlFileManager {

    private final String filePath;

    @XmlRootElement(name = "persons")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class PersonWrapper {
        @XmlElement(name = "person")
        private TreeSet<Person> persons = new TreeSet<>();

        public PersonWrapper() {
            // можно стереть нахер
            // но не повредит))
        }

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
     * @return Загруженная коллекция.
     * @throws IOException если произошла ошибка ввода-вывода.
     * @throws JAXBException если файл имеет неверный XML формат.
     * @throws FileNotFoundException если файл не найден.
     * @throws SecurityException если нет прав на чтение файла.
     */
    public TreeSet<Person> load() throws IOException, JAXBException, SecurityException {
        File file = new File(filePath);
        if (!file.exists()) {
            // Если файла нет, это не ошибка, а штатная ситуация. Просто возвращаем пустую коллекцию.
            // Сообщение об этом выведет вызывающий код (в Main).
            return new TreeSet<>();
        }
        // Проверки на isFile и canRead выбросят SecurityException, если что-то не так

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JAXBContext context = JAXBContext.newInstance(PersonWrapper.class, Person.class, Coordinates.class, Location.class, Color.class, Country.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            PersonWrapper wrapper = (PersonWrapper) unmarshaller.unmarshal(reader);
            return (wrapper != null && wrapper.getPersons() != null) ? wrapper.getPersons() : new TreeSet<>();
        }
    }

    /**
     * Сохраняет коллекцию в XML файл.
     * @param collection Коллекция для сохранения.
     * @throws IOException если произошла ошибка ввода-вывода.
     * @throws JAXBException если произошла ошибка при преобразовании в XML.
     * @throws SecurityException если нет прав на запись в файл.
     */
    public void save(TreeSet<Person> collection) throws IOException, JAXBException, SecurityException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            JAXBContext context = JAXBContext.newInstance(PersonWrapper.class, Person.class, Coordinates.class, Location.class, Color.class, Country.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            PersonWrapper wrapper = new PersonWrapper(collection);
            marshaller.marshal(wrapper, writer);
        }
    }
}