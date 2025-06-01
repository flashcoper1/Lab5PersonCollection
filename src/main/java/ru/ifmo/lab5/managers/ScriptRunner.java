package ru.ifmo.lab5.managers;

import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
// import org.jline.terminal.Terminal; // Больше не нужен здесь
import ru.ifmo.lab5.commands.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets; // Импорт для StandardCharsets
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Выполняет команды из скрипт-файла.
 * Реализует "тихий режим" для стандартного вывода команд, выполняемых из скрипта.
 * Ошибки выводятся в стандартный поток ошибок.
 */
public class ScriptRunner {
    private final CommandManager commandManager;
    // private final Terminal mainTerminal; // УДАЛЕНО
    private final Set<String> runningScripts = new HashSet<>();

    private final PrintStream originalSystemOut = System.out;
    private final PrintStream originalSystemErr = System.err;

    /**
     * Конструктор для {@code ScriptRunner}.
     *
     * @param commandManager менеджер команд для поиска и выполнения команд.
     */
    public ScriptRunner(CommandManager commandManager /*, Terminal mainTerminal */) { // mainTerminal убран из конструктора
        this.commandManager = commandManager;
        // this.mainTerminal = mainTerminal; // УДАЛЕНО
    }

    /**
     * Выполняет скрипт из указанного файла.
     * Стандартный вывод команд из скрипта подавляется. Ошибки выводятся в {@code System.err}.
     * Команды, требующие интерактивного ввода, будут использовать {@code UserInputHandler},
     * который был им передан при их создании.
     *
     * @param filePath путь к файлу скрипта.
     */
    public void executeScript(String filePath) {
        File scriptFile = new File(filePath);
        String absolutePath;
        try {
            absolutePath = scriptFile.getCanonicalPath();
        } catch (Exception e) {
            originalSystemErr.println("Ошибка при получении канонического пути к файлу скрипта '" + filePath + "': " + e.getMessage());
            return;
        }

        if (!scriptFile.exists() || !scriptFile.isFile()) {
            originalSystemErr.println("Файл скрипта не найден или не является файлом: " + absolutePath);
            return;
        }
        if (!scriptFile.canRead()) {
            originalSystemErr.println("Нет прав на чтение файла скрипта: " + absolutePath);
            return;
        }

        if (runningScripts.contains(absolutePath)) {
            originalSystemErr.println("Обнаружена рекурсия! Скрипт " + absolutePath + " уже выполняется.");
            return;
        }
        runningScripts.add(absolutePath);

        originalSystemOut.println("--- Начало выполнения скрипта (тихий режим для stdout): " + absolutePath + " ---");

        PrintStream dummyStream = new PrintStream(new OutputStream() {
            @Override public void write(int b) { /* Поглощаем */ }
        });
        PrintStream oldSystemOut = System.out;
        System.setOut(dummyStream);

        try (Scanner scriptScanner = new Scanner(scriptFile, StandardCharsets.UTF_8)) { // Используем StandardCharsets.UTF_8
            while (scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                originalSystemOut.println("СКРИПТ> " + line);

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String cmdArguments = parts.length > 1 ? parts[1] : null;

                Command command = commandManager.getCommand(commandName);
                if (command != null) {
                    try {
                        command.execute(cmdArguments);
                    } catch (EndOfFileException | UserInterruptException e) {
                        originalSystemErr.println("Ввод для команды '" + commandName + "' в скрипте был прерван. Выполнение скрипта остановлено.");
                        break;
                    } catch (Exception e) {
                        System.err.println("Ошибка при выполнении команды '" + commandName + "' из скрипта: " + e.getMessage());
                    }
                } else {
                    System.err.println("Неизвестная команда в скрипте: " + commandName);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл скрипта не найден: " + absolutePath);
        } catch (Exception e) {
            System.err.println("Критическая ошибка во время выполнения скрипта '" + absolutePath + "': " + e.getMessage());
            // Заменяем e.printStackTrace() на более контролируемый вывод или логирование
            logError(e); // Пример вызова метода логирования
        } finally {
            System.setOut(oldSystemOut);
            runningScripts.remove(absolutePath);
            originalSystemOut.println("--- Завершение выполнения скрипта: " + absolutePath + " ---");
        }
    }

    /**
     * Вспомогательный метод для логирования исключений.
     * Вместо прямого вызова e.printStackTrace().
     * @param e Исключение для логирования.
     */
    private void logError(Exception e) {
        originalSystemErr.println("Критическая ошибка: " + e.getMessage());
        for (StackTraceElement ste : e.getStackTrace()) {
            originalSystemErr.println("\t_ " + ste.toString());
        }
    }
}