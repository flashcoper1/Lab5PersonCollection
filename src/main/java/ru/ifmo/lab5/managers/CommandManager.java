package ru.ifmo.lab5.managers;

import ru.ifmo.lab5.commands.Command;
// import org.jline.terminal.Terminal; // Не нужен здесь напрямую
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner; // Был для старого ScriptRunner, теперь не нужен

/**
 * Управляет регистрацией и вызовом команд.
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final CollectionManager collectionManager;
    private final XmlFileManager xmlFileManager;
    private ScriptRunner scriptRunner; // Теперь устанавливается сеттером

    /**
     * Конструктор.
     * @param collectionManager Менеджер коллекции.
     * @param xmlFileManager Менеджер файлов.
     * @param consoleScanner Устаревший параметр, больше не используется напрямую здесь.
     */
    public CommandManager(CollectionManager collectionManager, XmlFileManager xmlFileManager, Scanner consoleScanner /*старый параметр*/) {
        this.collectionManager = collectionManager;
        this.xmlFileManager = xmlFileManager;
        // UserInputHandler и LineReader/Terminal будут передаваться командам при их создании/вызове
        // или через ConsoleApplication.
    }

    /**
     * Устанавливает исполнителя скриптов.
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
            // Это состояние не должно возникать при правильном порядке инициализации в Main
            throw new IllegalStateException("ScriptRunner не был инициализирован в CommandManager.");
        }
        return scriptRunner;
    }
}