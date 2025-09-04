package ru.ifmo.lab5.util;

import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

/**
 * Абстрактный интерфейс для поставщика ввода.
 * Позволяет {@link ru.ifmo.lab5.managers.UserInputHandler} читать данные
 * из разных источников (консоль, файл) без изменения своей логики.
 */
public interface InputProvider {
    /**
     * Читает следующую строку из источника.
     * @param prompt Приглашение к вводу (актуально только для консоли).
     * @return Прочитанная строка.
     * @throws UserInterruptException если ввод был прерван пользователем (Ctrl+C).
     * @throws EndOfFileException если достигнут конец потока ввода (Ctrl+D или конец файла).
     */
    String readLine(String prompt) throws UserInterruptException, EndOfFileException;
}