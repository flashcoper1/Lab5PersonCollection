package ru.ifmo.lab5.managers;

import ru.ifmo.lab5.commands.Command;
import ru.ifmo.lab5.util.CommandResult;
import ru.ifmo.lab5.util.CommandStatus;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Выполняет команды из скрипт-файла.
 * Отслеживает рекурсивные вызовы скриптов.
 */
public class ScriptRunner {
    private final CommandManager commandManager;
    private final Set<String> runningScripts = new HashSet<>();

    /**
     * Конструктор.
     * @param commandManager Менеджер команд для получения и выполнения команд.
     */
    public ScriptRunner(CommandManager commandManager) {
        this.commandManager = commandManager;
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

        if (!scriptFile.exists() || !scriptFile.isFile() || !scriptFile.canRead()) {
            System.err.println("Файл скрипта не найден, не является файлом или недоступен для чтения: " + absolutePath);
            return;
        }

        if (runningScripts.contains(absolutePath)) {
            System.err.println("Обнаружена рекурсия! Скрипт " + absolutePath + " уже выполняется.");
            return;
        }
        runningScripts.add(absolutePath);

        System.out.println("--- Начало выполнения скрипта: " + absolutePath + " ---");
        try (Scanner scriptScanner = new Scanner(scriptFile, "UTF-8")) {
            while (scriptScanner.hasNextLine()) {
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
        } finally {
            runningScripts.remove(absolutePath);
            System.out.println("--- Завершение выполнения скрипта: " + absolutePath + " ---");
        }
    }
}