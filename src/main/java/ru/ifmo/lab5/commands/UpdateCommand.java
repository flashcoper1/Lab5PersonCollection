package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Person;

/**
 * Команда для обновления элемента коллекции по ID.
 */
public class UpdateCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     * @param userInputHandler Обработчик пользовательского ввода.
     */
    public UpdateCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public void execute(String arguments) {
        if (arguments == null || arguments.trim().isEmpty()) {
            System.err.println("Ошибка: Необходимо указать ID элемента для обновления.");
            return;
        }
        try {
            long id = Long.parseLong(arguments.trim());
            if (collectionManager.getCollection().stream().noneMatch(p -> p.getId() == id)) {
                System.err.println("Ошибка: Человек с ID " + id + " не найден.");
                return;
            }

            System.out.println("Ввод новых данных для человека с ID " + id + ":");
            Person updatedPersonData = userInputHandler.requestPersonData();

            if (collectionManager.update(id, updatedPersonData)) {
                System.out.println("Человек с ID " + id + " успешно обновлен.");
            } else {
                System.err.println("Не удалось обновить человека с ID " + id + ". Возможно, он был удален другим процессом.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: ID должен быть числом.");
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка при обновлении: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка при вводе данных для команды update: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}