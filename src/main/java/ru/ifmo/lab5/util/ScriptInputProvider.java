package ru.ifmo.lab5.util;

import org.jline.reader.EndOfFileException;

import java.util.Scanner;

/**
 * Реализация {@link InputProvider} для чтения из файла с помощью Scanner.
 */
public class ScriptInputProvider implements InputProvider {
    private final Scanner scanner;

    /**
     * Конструктор.
     * @param scanner Сканер, связанный с файлом скрипта.
     */
    public ScriptInputProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String readLine(String prompt) {
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // Эмулируем эхо-ввод для наглядности выполнения скрипта
            System.out.println(line);
            return line;
        } else {
            throw new EndOfFileException();
        }
    }
}