package ru.ifmo.lab5.managers;

import org.jline.reader.EndOfFileException;
import ru.ifmo.lab5.commands.Command;
import ru.ifmo.lab5.util.CommandResult;
import ru.ifmo.lab5.util.CommandStatus;
import ru.ifmo.lab5.util.ScriptInputProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Выполняет команды из скрипт-файла.
 * Управляет стеком источников ввода в {@link UserInputHandler} для поддержки вложенных скриптов.
 */
public class ScriptRunner {
    private final CommandManager commandManager;
    private final UserInputHandler userInputHandler;
    private final Set<String> runningScripts = new HashSet<>();

    /**
     * Конструктор.
     * @param commandManager Менеджер команд для получения и выполнения команд.
     * @param userInputHandler Обработчик ввода для переключения на файловый источник.
     */
    public ScriptRunner(CommandManager commandManager, UserInputHandler userInputHandler) {
        this.commandManager = commandManager;
        this.userInputHandler = userInputHandler;
    }

    /**
     * Выполняет скрипт из указанного файла.
     * @param filePath Путь к файлу скрипта.
     */
    public void executeScript(String filePath) {
        String absolutePath;
        try {
            absolutePath = new File(filePath).getCanonicalPath();
        } catch (Exception e) {
            System.err.println("Ошибка при получении канонического пути к файлу скрипта '" + filePath + "': " + e.getMessage());
            return;
        }

        if (!new File(absolutePath).exists() || !new File(absolutePath).isFile() || !new File(absolutePath).canRead()) {
            System.err.println("Файл скрипта не найден, не является файлом или недоступен для чтения: " + absolutePath);
            return;
        }

        if (runningScripts.contains(absolutePath)) {
            System.err.println("Обнаружена рекурсия! Скрипт " + absolutePath + " уже выполняется.");
            return;
        }
        runningScripts.add(absolutePath);

        System.out.println("--- Начало выполнения скрипта: " + absolutePath + " ---");
        try (Scanner scriptScanner = new Scanner(new File(filePath), "UTF-8")) {

            userInputHandler.pushInputProvider(new ScriptInputProvider(scriptScanner));

            while (true) { // Бесконечный цикл, который прервется по EndOfFileException
                String line = scriptScanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                System.out.println("СКРИПТ> " + line);

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String cmdArguments = parts.length > 1 ? parts[1] : null;

                Command command = commandManager.getCommand(commandName);
                if (command != null) {
                    CommandResult result = command.execute(cmdArguments);
                    if (result.getStatus() == CommandStatus.SUCCESS) {
                        if (result.getMessage() != null && !result.getMessage().isEmpty()) {
                            System.out.println(result.getMessage());
                        }
                    } else {
                        System.err.println("Ошибка в скрипте: " + result.getMessage());
                    }
                } else {
                    System.err.println("Неизвестная команда в скрипте: " + commandName);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл скрипта не найден: " + absolutePath);
        } catch (java.util.NoSuchElementException e) {
            // Это штатное завершение работы Scanner'а, когда строки закончились
        } finally {
            runningScripts.remove(absolutePath);
            userInputHandler.popInputProvider();
            System.out.println("--- Завершение выполнения скрипта: " + absolutePath + " ---");
        }
    }
}