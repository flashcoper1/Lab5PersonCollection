package ru.ifmo.lab5.commands;
import ru.ifmo.lab5.managers.ScriptRunner;
import ru.ifmo.lab5.util.CommandResult;

/**
 * Команда для исполнения скрипта из указанного файла.
 * Делегирует выполнение {@link ScriptRunner}.
 */
public class ExecuteScriptCommand implements Command {
    private final ScriptRunner scriptRunner;

    /**
     * Конструктор команды.
     * @param scriptRunner Экземпляр исполнителя скриптов.
     */
    public ExecuteScriptCommand(ScriptRunner scriptRunner) {
        this.scriptRunner = scriptRunner;
    }


    @Override
    public CommandResult execute(String arguments) {
        if (arguments == null || arguments.trim().isEmpty()) {
            return CommandResult.error("Необходимо указать имя файла скрипта.");
        }
        scriptRunner.executeScript(arguments.trim());
        return CommandResult.success();
    }

    @Override
    public String getDescription() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла.";
    }
}