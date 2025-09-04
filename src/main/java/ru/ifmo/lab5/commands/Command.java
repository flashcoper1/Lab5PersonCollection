package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.util.CommandResult;

/**
 * Интерфейс, который должны реализовывать все команды.
 * Определяет основной метод для выполнения и метод для получения описания.
 */
public interface Command {
    /**
     * Выполняет логику команды.
     * @param arguments Аргументы, переданные команде в виде одной строки.
     * @return Объект {@link CommandResult}, содержащий статус и сообщение о результате выполнения.
     */
    CommandResult execute(String arguments);

    /**
     * Возвращает описание команды для использования в команде 'help'.
     * @return Строка с описанием команды.
     */
    String getDescription();
}