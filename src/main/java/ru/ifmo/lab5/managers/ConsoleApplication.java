package ru.ifmo.lab5.managers;

import org.jline.reader.*;
import org.jline.terminal.Terminal;
import ru.ifmo.lab5.commands.*;
import ru.ifmo.lab5.util.CommandResult;
import ru.ifmo.lab5.util.CommandStatus;

/**
 * Основной класс приложения. Управляет жизненным циклом,
 * читает команды из консоли, выполняет их и выводит результат.
 */
public class ConsoleApplication {
    private final CommandManager commandManager;
    private final UserInputHandler userInputHandler;
    private final LineReader lineReader;
    private boolean running = true;

    /**
     * Конструктор приложения.
     * @param commandManager Менеджер команд.
     * @param userInputHandler Обработчик пользовательского ввода.
     * @param lineReader JLine ридер для интерактивной консоли.
     */
    public ConsoleApplication(CommandManager commandManager, UserInputHandler userInputHandler,
                              LineReader lineReader) {
        this.commandManager = commandManager;
        this.userInputHandler = userInputHandler;
        this.lineReader = lineReader;
    }

    /**
     * Регистрирует все доступные команды в {@link CommandManager}.
     */
    public void registerCommands() {
        CollectionManager collectionManager = commandManager.getCollectionManager();
        XmlFileManager xmlFileManager = commandManager.getXmlFileManager();
        ScriptRunner scriptRunner = commandManager.getScriptRunner();

        commandManager.register("help", new HelpCommand(commandManager));
        commandManager.register("info", new InfoCommand(collectionManager));
        commandManager.register("show", new ShowCommand(collectionManager));
        commandManager.register("add", new AddCommand(collectionManager, userInputHandler));
        commandManager.register("update", new UpdateCommand(collectionManager, userInputHandler));
        commandManager.register("remove_by_id", new RemoveByIdCommand(collectionManager));
        commandManager.register("clear", new ClearCommand(collectionManager));
        commandManager.register("save", new SaveCommand(collectionManager, xmlFileManager));
        commandManager.register("exit", new ExitCommand(this));
        commandManager.register("add_if_min", new AddIfMinCommand(collectionManager, userInputHandler));
        commandManager.register("remove_greater", new RemoveGreaterCommand(collectionManager, userInputHandler));
        commandManager.register("remove_lower", new RemoveLowerCommand(collectionManager, userInputHandler));
        commandManager.register("average_of_height", new AverageOfHeightCommand(collectionManager));
        commandManager.register("count_by_hair_color", new CountByHairColorCommand(collectionManager, userInputHandler));
        commandManager.register("filter_less_than_hair_color", new FilterLessThanHairColorCommand(collectionManager, userInputHandler));
        commandManager.register("execute_script", new ExecuteScriptCommand(scriptRunner));
    }

    /**
     * Запускает главный цикл приложения.
     */
    public void run() {
        System.out.println("Консольное приложение для управления коллекцией Person.");
        System.out.println("Введите 'help' для получения списка команд. Используйте Tab для автодополнения.");

        while (running) {
            try {
                String line = lineReader.readLine("> ");
                if (line == null) { // Ctrl+D
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) continue;

                processCommand(line);

            } catch (UserInterruptException e) { // Ctrl+C
                System.out.println("\n^C. Для выхода введите 'exit'.");
            } catch (EndOfFileException e) { // Ctrl+D
                break;
            }
        }
    }

    /**
     * Обрабатывает одну строку с командой.
     * @param line Введенная пользователем строка.
     */
    public void processCommand(String line) {
        String[] parts = line.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : null;

        Command command = commandManager.getCommand(commandName);
        if (command != null) {
            CommandResult result = command.execute(arguments);

            if (result.getStatus() == CommandStatus.SUCCESS) {
                if (result.getMessage() != null && !result.getMessage().isEmpty()) {
                    System.out.println(result.getMessage());
                }
            } else {
                System.err.println("Ошибка: " + result.getMessage());
            }
        } else {
            System.err.println("Неизвестная команда: " + commandName);
        }
    }

    /**
     * Устанавливает флаг работы приложения. Используется командой 'exit'.
     * @param running Новое состояние флага.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}