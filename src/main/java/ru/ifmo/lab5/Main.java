package ru.ifmo.lab5;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.ifmo.lab5.commands.ExecuteScriptCommand;
import ru.ifmo.lab5.managers.*;
import ru.ifmo.lab5.util.CommandCompleter;

import java.io.IOException;

/**
 * Главный класс приложения. Инициализирует все компоненты и запускает консольное приложение.
 */
public class Main {
    /**
     * Точка входа в программу.
     * Инициализирует менеджеры, JLine для консольного ввода, регистрирует команды
     * и запускает основной цикл обработки команд.
     * Ожидает путь к файлу коллекции через переменную окружения PERSON_COLLECTION_FILE.
     *
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        String filePath = System.getenv("PERSON_COLLECTION_FILE");
        if (filePath == null || filePath.trim().isEmpty()) {
            System.err.println("Ошибка: Переменная окружения PERSON_COLLECTION_FILE не установлена или пуста.");
            System.err.println("Пожалуйста, установите ее и укажите путь к XML файлу коллекции.");
            System.err.println("Пример (Linux/macOS): export PERSON_COLLECTION_FILE=\"/путь/к/вашему/файлу.xml\"");
            System.err.println("Пример (Windows CMD): set PERSON_COLLECTION_FILE=\"C:\\путь\\к\\вашему\\файлу.xml\"");
            System.err.println("Пример (Windows PowerShell): $env:PERSON_COLLECTION_FILE=\"C:\\путь\\к\\вашему\\файлу.xml\"");
            return;
        }

        Terminal terminal = null;
        // ConsoleApplication app; // Инициализатор 'null' избыточен, убрали

        try {
            // 1. Создаем Terminal
            terminal = TerminalBuilder.builder().system(true).build();

            // 2. Создаем базовые менеджеры
            CollectionManager collectionManager = new CollectionManager();
            XmlFileManager xmlFileManager = new XmlFileManager(filePath);
            collectionManager.setCollection(xmlFileManager.load());

            // 3. CommandManager
            CommandManager commandManager = new CommandManager(collectionManager, xmlFileManager, null);

            // 4. Создаем наш кастомный CommandCompleter СРАЗУ после CommandManager
            CommandCompleter commandCompleter = new CommandCompleter(commandManager);

            // 5. Создаем LineReader и СРАЗУ устанавливаем ему наш CommandCompleter
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(commandCompleter)
                    .history(new org.jline.reader.impl.history.DefaultHistory())
                    .variable(LineReader.HISTORY_FILE, ".person_app_history")
                    .build();

            // 6. Создаем UserInputHandler с готовым LineReader
            UserInputHandler userInputHandler = new UserInputHandler(lineReader);

            // 7. Создаем ScriptRunner
            ScriptRunner scriptRunner = new ScriptRunner(commandManager); // Убрали terminal из конструктора
            commandManager.setScriptRunner(scriptRunner);

            // 8. Создаем ConsoleApplication
            ConsoleApplication app = new ConsoleApplication( // Объявляем и инициализируем здесь
                    collectionManager,
                    commandManager,
                    xmlFileManager,
                    userInputHandler,
                    lineReader,
                    terminal
            );

            // 9. Регистрируем ВСЕ команды через ConsoleApplication
            app.registerCommands();
            commandManager.register("execute_script", new ExecuteScriptCommand(scriptRunner));

            // 10. Комплетер уже установлен.

            app.run();

        } catch (IOException e) {
            System.err.println("Критическая ошибка при инициализации JLine терминала: " + e.getMessage());
            logError(e); // Используем свой метод логирования
        } catch (Exception e) { // Общий перехват на случай других ошибок инициализации
            System.err.println("Непредвиденная критическая ошибка при запуске приложения: " + e.getMessage());
            logError(e);
        }
        finally {
            if (terminal != null) {
                try {
                    terminal.close();
                } catch (IOException e) {
                    System.err.println("Ошибка при закрытии терминала: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Вспомогательный метод для логирования исключений в Main.
     * @param e Исключение для логирования.
     */
    private static void logError(Exception e) {
        // В реальном приложении здесь мог бы быть вызов логгера
        System.err.println("Подробности ошибки: ");
        for (StackTraceElement ste : e.getStackTrace()) {
            System.err.println("\t_ " + ste.toString());
        }
    }
}