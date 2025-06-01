package ru.ifmo.lab5;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
// import org.jline.reader.impl.completer.StringsCompleter; // Больше не нужен для основного комплетера
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.ifmo.lab5.commands.ExecuteScriptCommand;
import ru.ifmo.lab5.managers.*; // Импортируем CommandCompleter
import ru.ifmo.lab5.util.*;
import java.io.IOException;
// import java.util.stream.Collectors; // Больше не нужен для StringsCompleter

public class Main {
    public static void main(String[] args) {
        String filePath = System.getenv("PERSON_COLLECTION_FILE");
        if (filePath == null || filePath.trim().isEmpty()) {
            System.err.println("Ошибка: Переменная окружения PERSON_COLLECTION_FILE не установлена или пуста.");
            // ... (сообщения пользователю)
            return;
        }

        Terminal terminal = null;
        ConsoleApplication app = null;

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
                    .completer(commandCompleter) // Используем наш кастомный комплетер
                    .history(new org.jline.reader.impl.history.DefaultHistory())
                    .variable(LineReader.HISTORY_FILE, ".person_app_history")
                    .build();

            // 6. Создаем UserInputHandler с готовым LineReader
            UserInputHandler userInputHandler = new UserInputHandler(lineReader);

            // 7. Создаем ScriptRunner
            ScriptRunner scriptRunner = new ScriptRunner(commandManager, terminal);
            commandManager.setScriptRunner(scriptRunner);

            // 8. Создаем ConsoleApplication
            app = new ConsoleApplication(
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

            // 10. Комплетер уже установлен и будет динамически брать команды из commandManager.
            // Строка lineReader.setCompleter(...) больше не нужна и удалена.

            app.run();

        } catch (IOException e) {
            System.err.println("Критическая ошибка при инициализации JLine терминала: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (terminal != null) {
                try {
                    terminal.close();
                } catch (IOException e) {
                    System.err.println("Ошибка при закрытии терминала: " + e.getMessage());
                }
            }
        }
    }
}