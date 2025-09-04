package ru.ifmo.lab5;

import jakarta.xml.bind.JAXBException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.ifmo.lab5.managers.*;
import ru.ifmo.lab5.model.Person;
import ru.ifmo.lab5.util.CommandCompleter;
import ru.ifmo.lab5.util.ConsoleInputProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Главный класс приложения.
 * Инициализирует все компоненты и запускает консольное приложение.
 */
public class Main {
    /**
     * Точка входа в программу.
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        String filePath = System.getenv("PERSON_COLLECTION_FILE");
        if (filePath == null || filePath.trim().isEmpty()) {
            System.err.println("Ошибка: Переменная окружения PERSON_COLLECTION_FILE не установлена.");
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
                System.err.println("Критическая ошибка при загрузке коллекции из файла. Проверьте содержимое файла и права доступа.");
                // ИСПРАВЛЕНИЕ: Выводим полный стектрейс для диагностики
                e.printStackTrace();
                System.err.println("Программа будет завершена.");
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

            UserInputHandler userInputHandler = new UserInputHandler();
            userInputHandler.pushInputProvider(new ConsoleInputProvider(lineReader));

            ScriptRunner scriptRunner = new ScriptRunner(commandManager, userInputHandler);
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
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Непредвиденная критическая ошибка при запуске приложения: " + e.getMessage());
            e.printStackTrace();
        }
    }
}