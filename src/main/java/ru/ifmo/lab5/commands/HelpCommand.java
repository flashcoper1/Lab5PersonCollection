package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CommandManager;
import ru.ifmo.lab5.util.CommandResult;
import java.util.Map;
/**
 * Команда для вывода справки по доступным командам.
 */
public class HelpCommand implements Command {
    private final CommandManager commandManager;

    /**
     * Конструктор.
     * @param commandManager Менеджер команд для получения списка команд.
     */
    public HelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public CommandResult execute(String arguments) {
        StringBuilder sb = new StringBuilder("Доступные команды:\n");
        // Используем форматирование для выравнивания колонок
        commandManager.getCommands().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        sb.append(String.format("  %-35s - %s\n", entry.getKey(), entry.getValue().getDescription()))
                );
        return CommandResult.success(sb.toString().trim());
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}