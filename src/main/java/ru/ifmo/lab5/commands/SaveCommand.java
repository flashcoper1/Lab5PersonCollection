package ru.ifmo.lab5.commands;

import jakarta.xml.bind.JAXBException;
import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.XmlFileManager;
import ru.ifmo.lab5.util.CommandResult;
import java.io.IOException;

public class SaveCommand implements Command {
    private final CollectionManager collectionManager;
    private final XmlFileManager xmlFileManager;

    public SaveCommand(CollectionManager collectionManager, XmlFileManager xmlFileManager) {
        this.collectionManager = collectionManager;
        this.xmlFileManager = xmlFileManager;
    }

    @Override
    public CommandResult execute(String arguments) {
        try {
            xmlFileManager.save(collectionManager.getCollection());
            return CommandResult.success("Коллекция успешно сохранена в файл.");
        } catch (JAXBException e) {
            return CommandResult.error("Ошибка при преобразовании коллекции в XML: " + e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("Ошибка ввода-вывода при записи в файл: " + e.getMessage());
        } catch (SecurityException e) {
            return CommandResult.error("Ошибка прав доступа: не удалось записать в файл.");
        }
    }

    @Override
    public String getDescription() {
        return "save : сохранить коллекцию в файл";
    }
}