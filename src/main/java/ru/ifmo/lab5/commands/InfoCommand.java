package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;

/**
 * Команда для вывода информации о коллекции.
 */
public class InfoCommand implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     */
    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String arguments) {
        System.out.println(collectionManager.getInfo());
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}