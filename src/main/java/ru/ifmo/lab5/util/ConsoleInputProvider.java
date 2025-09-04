package ru.ifmo.lab5.util;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;

/**
 * Реализация {@link InputProvider} для чтения из интерактивной консоли с помощью JLine.
 */
public class ConsoleInputProvider implements InputProvider {
    private final LineReader lineReader;

    /**
     * Конструктор.
     * @param lineReader JLine ридер.
     */
    public ConsoleInputProvider(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    @Override
    public String readLine(String prompt) throws UserInterruptException, EndOfFileException {
        return this.lineReader.readLine(prompt);
    }
}
