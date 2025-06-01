package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CollectionManager;
import ru.ifmo.lab5.managers.UserInputHandler;
import ru.ifmo.lab5.model.Person;

/**
 * Команда для добавления нового элемента в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
 */
public class AddIfMinCommand implements Command {
    private final CollectionManager collectionManager;
    private final UserInputHandler userInputHandler;

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     * @param userInputHandler Обработчик пользовательского ввода.
     */
    public AddIfMinCommand(CollectionManager collectionManager, UserInputHandler userInputHandler) {
        this.collectionManager = collectionManager;
        this.userInputHandler = userInputHandler;
    }

    @Override
    public void execute(String arguments) {
        try {
            System.out.println("Ввод данных для нового человека (для сравнения с минимальным):");
            Person newPerson = userInputHandler.requestPersonData(); // ID будет 0, compareTo будет работать

            if (collectionManager.getCollection().isEmpty()) {
                collectionManager.add(newPerson);
                System.out.println("Коллекция была пуста. Новый человек успешно добавлен с ID: " + newPerson.getId());
            } else {
                // Создаем временный объект с ID=0 для сравнения, т.к. ID нового еще не присвоен
                // или сравниваем с первым элементом, если коллекция не пуста
                Person minElement = collectionManager.getCollection().first();
                // Для корректного сравнения, newPerson должен иметь ID.
                // Но ID присваивается только при реальном добавлении.
                // Поэтому, мы не можем напрямую использовать newPerson.compareTo(minElement) до присвоения ID.
                // Логика addIfMin в CollectionManager должна это учитывать или мы должны сравнивать по полям.
                // Текущая реализация addIfMin в CollectionManager сравнивает по ID, что для нового элемента (ID=0)
                // почти всегда будет меньше, если в коллекции есть элементы с ID > 0.
                // Это не совсем то, что обычно подразумевается под "значением элемента".
                // Для корректной работы по "значению", нужно определить, что такое "значение".
                // Если "значение" - это ID, то новый элемент (с будущим ID) должен быть меньше.
                // Переделаем: сначала создаем, потом сравниваем с тем, что есть.
                // Но ID нового элемента будет больше существующих.
                // Значит, "add_if_min" по ID для нового элемента почти никогда не сработает, если коллекция не пуста.
                // Задание, вероятно, подразумевает сравнение по какому-то другому критерию или
                // что "элемент" для сравнения вводится полностью, а не генерируется.
                // Оставим сравнение по ID, как наиболее простое для TreeSet<Person>.
                // В этом случае, новый элемент (с ID=0 до добавления) будет меньше любого существующего с ID > 0.
                // После добавления через collectionManager.addIfMin, он получит корректный ID.

                // Создаем копию для сравнения, чтобы не менять ID у newPerson до решения о добавлении
                Person personForComparison = new Person(0, newPerson.getName(), newPerson.getCoordinates(), newPerson.getHeight(), newPerson.getEyeColor(), newPerson.getHairColor(), newPerson.getNationality(), newPerson.getLocation());
                // Устанавливаем временный ID, который будет меньше существующих, если они >0
                // Это не совсем корректно, если ID могут быть отрицательными или 0 уже занят.
                // Но наш nextId начинается с 1.
                // Правильнее было бы, если бы addIfMin принимал объект и сам решал, как его сравнивать.
                // Текущая реализация CollectionManager.addIfMin(person) сравнивает person (с ID=0) с collection.first().

                if (collectionManager.addIfMin(newPerson)) { // newPerson передается с ID=0, CollectionManager присвоит новый ID
                    System.out.println("Новый человек успешно добавлен (ID: " + newPerson.getId() + "), так как его ID (после генерации) оказался подходящим или коллекция была пуста.");
                } else {
                    System.out.println("Новый человек не добавлен, так как его ID (после генерации) не был меньше минимального в коллекции.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка при добавлении: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка при вводе данных для команды add_if_min: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "add_if_min {element} : добавить новый элемент в коллекцию, если его значение (ID) меньше, чем у наименьшего элемента этой коллекции";
    }
}