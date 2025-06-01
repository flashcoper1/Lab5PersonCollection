package ru.ifmo.lab5.commands;

// Terminal и UserInputHandler больше не нужны в конструкторе, ScriptRunner их уже знает
import ru.ifmo.lab5.managers.ScriptRunner;

/**
 * Команда для выполнения скрипта из файла.
 */
public class ExecuteScriptCommand implements Command {
    private final ScriptRunner scriptRunner;

    /**
     * Конструктор.
     * @param scriptRunner Исполнитель скриптов.
     */
    public ExecuteScriptCommand(ScriptRunner scriptRunner) {
        this.scriptRunner = scriptRunner;
    }

    @Override
    public void execute(String arguments) {
        if (arguments == null || arguments.trim().isEmpty()) {
            System.err.println("Ошибка: Необходимо указать имя файла скрипта.");
            return;
        }
        // ScriptRunner уже имеет доступ к mainTerminal и mainUserInputHandler,
        // которые были переданы ему при создании в Main.java
        scriptRunner.executeScript(arguments.trim());
    }

    @Override
    public String getDescription() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла.";
    }
}