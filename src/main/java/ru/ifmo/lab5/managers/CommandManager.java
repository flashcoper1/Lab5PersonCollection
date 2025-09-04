package ru.ifmo.lab5.managers;

import ru.ifmo.lab5.commands.Command;
import java.util.HashMap;
import java.util.Map;

/**
 * Управляет регистрацией и вызовом команд.
 * Хранит карту команд, сопоставляя их имена с объектами команд.
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final CollectionManager collectionManager;
    private final XmlFileManager xmlFileManager;
    private ScriptRunner scriptRunner; // Теперь устанавливается сеттером

    /**
     * Конструктор менеджера команд.
     * @param collectionManager Менеджер коллекции, передается командам.
     * @param xmlFileManager Менеджер файлов, передается командам.
     */
    public CommandManager(CollectionManager collectionManager, XmlFileManager xmlFileManager) {
        this.collectionManager = collectionManager;
        this.xmlFileManager = xmlFileManager;
    }

    /**
     * Устанавливает исполнителя скриптов. Необходимо для команды execute_script.
     * @param scriptRunner Исполнитель скриптов.
     */
    public void setScriptRunner(ScriptRunner scriptRunner) {
        this.scriptRunner = scriptRunner;
    }

    /**
     * Регистрирует команду.
     * @param commandName Имя команды.
     * @param command Объект команды.
     */
    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Возвращает команду по имени.
     * @param commandName Имя команды.
     * @return Объект команды или null, если команда не найдена.
     */
    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    /**
     * Возвращает карту всех зарегистрированных команд.
     * @return Карта команд.
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    // Геттеры для зависимостей, которые нужны командам при их создании в ConsoleApplication
    public CollectionManager getCollectionManager() { return collectionManager; }
    public XmlFileManager getXmlFileManager() { return xmlFileManager; }

    /**
     * Возвращает исполнителя скриптов.
     * @return Исполнитель скриптов.
     * @throws IllegalStateException если ScriptRunner не был инициализирован.
     */
    public ScriptRunner getScriptRunner() {
        if (this.scriptRunner == null) {
            throw new IllegalStateException("ScriptRunner не был инициализирован в CommandManager.");
        }
        return scriptRunner;
    }
}