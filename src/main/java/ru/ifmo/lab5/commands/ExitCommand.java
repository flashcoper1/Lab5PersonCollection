package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.ConsoleApplication;

/**
 * Команда для завершения программы.
 */
public class ExitCommand implements Command {
    private final ConsoleApplication app;

    /**
     * Конструктор.
     * @param app Ссылка на основной класс приложения для управления его состоянием.
     */
    public ExitCommand(ConsoleApplication app) {
        this.app = app;
    }

    @Override
    public void execute(String arguments) {
        System.out.println("Завершение программы (без сохранения)...");
        app.setRunning(false); // Устанавливаем флаг для выхода из основного цикла
    }

    @Override
    public String getDescription() {
        return "exit : завершить программу (без сохранения в файл)";
    }
}