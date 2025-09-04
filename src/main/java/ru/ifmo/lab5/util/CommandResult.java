package ru.ifmo.lab5.util;

/**
 * Класс-обертка для результата выполнения команды.
 * Инкапсулирует статус выполнения и сообщение для пользователя
 */
public class CommandResult {
    private final CommandStatus status;
    private final String message;

    /**
     * Приватный конструктор для создания экземпляра через статические методы.
     * @param status Статус выполнения.
     * @param message Сообщение для пользователя.
     */
    private CommandResult(CommandStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Создает успешный результат с сообщением.
     * @param message Сообщение для пользователя.
     * @return Объект CommandResult со статусом SUCCESS.
     */
    public static CommandResult success(String message) {
        return new CommandResult(CommandStatus.SUCCESS, message);
    }

    /**
     * Создает успешный результат без сообщения (для команд, которым нечего выводить, например, save).
     * @return Объект CommandResult со статусом SUCCESS.
     */
    public static CommandResult success() {
        return new CommandResult(CommandStatus.SUCCESS, null);
    }

    /**
     * Создает результат с ошибкой.
     * @param message Сообщение об ошибке.
     * @return Объект CommandResult со статусом ERROR.
     */
    public static CommandResult error(String message) {
        return new CommandResult(CommandStatus.ERROR, message);
    }

    /**
     * Возвращает статус выполнения команды.
     * @return {@link CommandStatus}
     */
    public CommandStatus getStatus() {
        return status;
    }

    /**
     * Возвращает сообщение для пользователя.
     * @return Строка с сообщением или null, если сообщения нет.
     */
    public String getMessage() {
        return message;
    }
}