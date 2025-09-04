package ru.ifmo.lab5.commands;
import ru.ifmo.lab5.managers.ScriptRunner;
import ru.ifmo.lab5.util.CommandResult;

public class ExecuteScriptCommand implements Command {
    private final ScriptRunner scriptRunner;

    public ExecuteScriptCommand(ScriptRunner scriptRunner) {
        this.scriptRunner = scriptRunner;
    }

    @Override
    public CommandResult execute(String arguments) {
        if (arguments == null || arguments.trim().isEmpty()) {
            return CommandResult.error("Необходимо указать имя файла скрипта.");
        }
        scriptRunner.executeScript(arguments.trim());
        return CommandResult.success(); // Сообщения о выполнении выводятся самим ScriptRunner
    }

    @Override
    public String getDescription() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла.";
    }
}