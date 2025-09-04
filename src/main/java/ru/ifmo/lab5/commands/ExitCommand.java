package ru.ifmo.lab5.commands;
import ru.ifmo.lab5.managers.ConsoleApplication;
import ru.ifmo.lab5.util.CommandResult;

public class ExitCommand implements Command {
    private final ConsoleApplication app;

    public ExitCommand(ConsoleApplication app) {
        this.app = app;
    }

    @Override
    public CommandResult execute(String arguments) {
        app.setRunning(false);
        return CommandResult.success("Завершение программы...");
    }

    @Override
    public String getDescription() {
        return "exit : завершить программу (без сохранения в файл)";
    }
}