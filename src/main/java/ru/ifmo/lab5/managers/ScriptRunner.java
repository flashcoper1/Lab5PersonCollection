package ru.ifmo.lab5.managers;

import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import ru.ifmo.lab5.commands.Command;
// ExecuteScriptCommand не нужен здесь как зависимость конструктора

import java.io.File;
import java.io.FileNotFoundException;
// import java.io.IOException; // Не используется напрямую здесь
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Выполняет команды из скрипт-файла.
 */
public class ScriptRunner {
    private final CommandManager commandManager;
    private final UserInputHandler mainUserInputHandler; // Для интерактивного ввода из скрипта
    private final Terminal mainTerminal; // Для передачи вложенным ExecuteScriptCommand
    private final Set<String> runningScripts = new HashSet<>();

    /**
     * Конструктор.
     * @param commandManager Менеджер команд.
     * @param mainUserInputHandler Основной обработчик ввода (с JLine консоли).
     * @param mainTerminal Основной терминал.
     */
    public ScriptRunner(CommandManager commandManager, UserInputHandler mainUserInputHandler, Terminal mainTerminal) {
        this.commandManager = commandManager;
        this.mainUserInputHandler = mainUserInputHandler; // Сохраняем для команд, требующих ввода
        this.mainTerminal = mainTerminal; // Сохраняем для передачи вложенным скриптам
    }

    /**
     * Выполняет скрипт из указанного файла.
     * @param filePath Путь к файлу скрипта.
     */
    public void executeScript(String filePath) {
        File scriptFile = new File(filePath);
        String absolutePath;
        try {
            absolutePath = scriptFile.getCanonicalPath();
        } catch (Exception e) {
            System.err.println("Ошибка при получении канонического пути к файлу скрипта '" + filePath + "': " + e.getMessage());
            return;
        }

        if (!scriptFile.exists() || !scriptFile.isFile()) {
            System.err.println("Файл скрипта не найден или не является файлом: " + absolutePath);
            return;
        }
        if (!scriptFile.canRead()) {
            System.err.println("Нет прав на чтение файла скрипта: " + absolutePath);
            return;
        }

        if (runningScripts.contains(absolutePath)) {
            System.err.println("Обнаружена рекурсия! Скрипт " + absolutePath + " уже выполняется.");
            return;
        }
        runningScripts.add(absolutePath);

        System.out.println("--- Начало выполнения скрипта: " + absolutePath + " ---");
        // UserInputHandler для команд из скрипта будет mainUserInputHandler,
        // что означает, что если команда (например, add) потребует ввода, он будет из консоли.
        try (Scanner scriptScanner = new Scanner(scriptFile, "UTF-8")) {
            while (scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                System.out.println("СКРИПТ> " + line);

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String cmdArguments = parts.length > 1 ? parts[1] : null;

                Command command = commandManager.getCommand(commandName);
                if (command != null) {
                    try {
                        // Команды, созданные в ConsoleApplication, уже имеют ссылку на mainUserInputHandler
                        // через CommandManager -> ConsoleApplication -> конструкторы команд.
                        // ExecuteScriptCommand получит этот ScriptRunner (this) и вызовет executeScript рекурсивно.
                        command.execute(cmdArguments);
                    } catch (EndOfFileException e) {
                        System.err.println("Ввод для команды '" + commandName + "' в скрипте был прерван (EOF). Выполнение скрипта остановлено.");
                        break;
                    } catch (UserInterruptException e) {
                        System.err.println("Ввод для команды '" + commandName + "' в скрипте был прерван (Ctrl+C). Выполнение скрипта остановлено.");
                        break;
                    } catch (Exception e) {
                        System.err.println("Ошибка при выполнении команды '" + commandName + "' из скрипта: " + e.getMessage());
                        // e.printStackTrace(); // Для отладки
                    }
                } else {
                    System.err.println("Неизвестная команда в скрипте: " + commandName);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл скрипта не найден (неожиданно после проверок): " + absolutePath);
        } catch (Exception e) {
            System.err.println("Критическая ошибка во время выполнения скрипта '" + absolutePath + "': " + e.getMessage());
            e.printStackTrace();
        } finally {
            runningScripts.remove(absolutePath);
            System.out.println("--- Завершение выполнения скрипта: " + absolutePath + " ---");
        }
    }
}