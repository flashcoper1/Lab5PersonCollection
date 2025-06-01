package ru.ifmo.lab5.managers;

import org.jline.reader.*;
import org.jline.terminal.Terminal;
import ru.ifmo.lab5.commands.*;

import java.io.IOException; // Для Terminal.close()

/**
 * Основной класс приложения, управляющий циклом ввода команд.
 */
public class ConsoleApplication {
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private final XmlFileManager xmlFileManager;
    private final UserInputHandler userInputHandler;
    private final LineReader lineReader;
    private final Terminal terminal; // Сохраняем для корректного закрытия
    private boolean running = true;

    /**
     * Конструктор.
     */
    public ConsoleApplication(CollectionManager collectionManager, CommandManager commandManager,
                              XmlFileManager xmlFileManager, UserInputHandler userInputHandler,
                              LineReader lineReader, Terminal terminal) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
        this.xmlFileManager = xmlFileManager;
        this.userInputHandler = userInputHandler;
        this.lineReader = lineReader;
        this.terminal = terminal;
    }

    /**
     * Регистрирует все команды в CommandManager.
     * Этот метод должен вызываться после создания всех менеджеров.
     */
    public void registerCommands() {
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
        // ExecuteScriptCommand регистрируется в Main
    }

    public void run() {
        System.out.println("Консольное приложение для управления коллекцией Person (с JLine).");
        System.out.println("Введите 'help' для получения списка команд. Используйте Tab для автодополнения, стрелки вверх/вниз для истории.");

        String line;

        while (running) {
            try {
                line = lineReader.readLine("> ");
                if (line == null) {
                    System.out.println("\nПолучен сигнал EOF (Ctrl+D).");
                    handleExitRequest(true);
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                processCommand(line);
            } catch (UserInterruptException e) {
                System.out.println("^C");
            } catch (EndOfFileException e) {
                System.out.println("\nВвод завершен (EOF).");
                handleExitRequest(true);
                break;
            } catch (Exception e) {
                System.err.println("Произошла непредвиденная ошибка в главном цикле: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Программа завершена.");
    }

    private void handleExitRequest(boolean saveOnExit) {
        if (saveOnExit) {
            System.out.println("Попытка сохранить коллекцию перед выходом...");
            xmlFileManager.save(collectionManager.getCollection());
        } else {
            System.out.println("Завершение программы без сохранения (по команде exit).");
        }
    }

    public void processCommand(String line) {
        String[] parts = line.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : null;

        Command command = commandManager.getCommand(commandName);
        if (command != null) {
            try {
                command.execute(arguments);
            } catch (EndOfFileException e) {
                System.err.println("Ввод для команды '" + commandName + "' был прерван (EOF).");
            } catch (UserInterruptException e) {
                System.err.println("Ввод для команды '" + commandName + "' был прерван (Ctrl+C).");
            }
            catch (Exception e) {
                System.err.println("Ошибка при выполнении команды '" + commandName + "': " + e.getMessage());
            }
        } else {
            System.err.println("Неизвестная команда: " + commandName + ". Введите 'help' для списка команд.");
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}