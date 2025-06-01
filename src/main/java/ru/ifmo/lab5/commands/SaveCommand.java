package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.XmlFileManager;

/**
 * Команда для сохранения коллекции в файл.
 */
public class SaveCommand implements Command {
    private final CollectionManager collectionManager;
    private final XmlFileManager xmlFileManager;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     * @param xmlFileManager Менеджер файлов.
     */
    public SaveCommand(CollectionManager collectionManager, XmlFileManager xmlFileManager) {
        this.collectionManager = collectionManager;
        this.xmlFileManager = xmlFileManager;
    }

    @Override
    public void execute(String arguments) {
        xmlFileManager.save(collectionManager.getCollection());
    }

    @Override
    public String getDescription() {
        return "save : сохранить коллекцию в файл";
    }
}