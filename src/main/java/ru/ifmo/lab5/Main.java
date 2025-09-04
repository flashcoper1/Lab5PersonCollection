package ru.ifmo.lab5;

import jakarta.xml.bind.JAXBException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.ifmo.lab5.managers.*;
import ru.ifmo.lab5.model.Person;
import ru.ifmo.lab5.util.CommandCompleter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Главный класс приложения.
 * Инициализирует все компоненты: менеджеры, JLine для консольного ввода,
 * регистрирует команды и запускает основной цикл обработки команд.
 */
public class Main {
    /**
     * Точка входа в программу.
     * <p>
     * Выполняет следующие шаги:
     * 1. Проверяет наличие переменной окружения с путем к файлу коллекции.
     * 2. Инициализирует JLine Terminal.
     * 3. Создает {@link XmlFileManager} и {@link CollectionManager}.
     * 4. Загружает коллекцию из файла, обрабатывая возможные ошибки.
     * 5. Создает {@link CommandManager}, {@link UserInputHandler}, {@link ScriptRunner}.
     * 6. Создает {@link ConsoleApplication} и регистрирует в нем все команды.
     * 7. Запускает главный цикл приложения.
     *
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        String filePath = System.getenv("PERSON_COLLECTION_FILE");
        if (filePath == null || filePath.trim().isEmpty()) {
            System.err.println("Ошибка: Переменная окружения PERSON_COLLECTION_FILE не установлена или пуста.");
            return;
        }

        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {

            CollectionManager collectionManager = new CollectionManager();
            XmlFileManager xmlFileManager = new XmlFileManager(filePath);

            try {
                TreeSet<Person> loadedCollection = xmlFileManager.load();
                collectionManager.setCollection(loadedCollection);
                System.out.println("Коллекция успешно загружена. Загружено элементов: " + loadedCollection.size());
            } catch (FileNotFoundException e) {
                System.out.println("Файл коллекции не найден. Будет создана новая пустая коллекция.");
            } catch (JAXBException | IOException | SecurityException e) {
                System.err.println("Критическая ошибка при загрузке коллекции из файла: " + e.getMessage());
                System.err.println("Программа будет завершена, так как начальное состояние не может быть загружено.");
                return;
            }

            CommandManager commandManager = new CommandManager(collectionManager, xmlFileManager);
            CommandCompleter commandCompleter = new CommandCompleter(commandManager);

            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(commandCompleter)
                    .history(new org.jline.reader.impl.history.DefaultHistory())
                    .variable(LineReader.HISTORY_FILE, ".person_app_history")
                    .build();

            UserInputHandler userInputHandler = new UserInputHandler(lineReader);
            ScriptRunner scriptRunner = new ScriptRunner(commandManager);
            commandManager.setScriptRunner(scriptRunner);

            ConsoleApplication app = new ConsoleApplication(
                    commandManager,
                    userInputHandler,
                    lineReader
            );

            app.registerCommands();
            app.run();

        } catch (IOException e) {
            System.err.println("Критическая ошибка терминала: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Непредвиденная критическая ошибка при запуске приложения: " + e.getMessage());
        }
    }
}