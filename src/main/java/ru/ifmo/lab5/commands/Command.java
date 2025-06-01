package ru.ifmo.lab5.commands;

/**
 * Интерфейс для всех команд.
 */
public interface Command {
    /**
     * Выполняет команду.
     * @param arguments Аргументы команды.
     */
    void execute(String arguments);

    /**
     * Возвращает описание команды.
     * @return Описание команды.
     */
    String getDescription();
}