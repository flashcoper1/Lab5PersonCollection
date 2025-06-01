package ru.ifmo.lab5.commands;

import ru.ifmo.lab5.managers.CommandManager;

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
    public void execute(String arguments) {
        System.out.println("Доступные команды:");
        // Сортируем команды по имени для более удобного вывода
        commandManager.getCommands().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println("  " + entry.getKey() + " - " + entry.getValue().getDescription())
                );
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}