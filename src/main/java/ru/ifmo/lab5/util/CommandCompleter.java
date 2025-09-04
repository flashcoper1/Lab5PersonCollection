package ru.ifmo.lab5.util;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import ru.ifmo.lab5.managers.CommandManager;

import java.util.List;

/**
 * Реализация интерфейса {@link Completer} для автодополнения имен команд в JLine.
 * Динамически получает список доступных команд из {@link CommandManager}
 * и предлагает варианты, начинающиеся с уже введенной пользователем части команды.
 */
public class CommandCompleter implements Completer {

    private final CommandManager commandManager;

    /**
     * Конструктор {@code CommandCompleter}.
     *
     * @param commandManager менеджер команд, из которого будут извлекаться
     *                       имена команд для предоставления вариантов автодополнения.
     */
    public CommandCompleter(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Заполняет список кандидатов для автодополнения на основе текущего ввода пользователя.
     * Этот метод вызывается библиотекой JLine при нажатии пользователем клавиши автодополнения (обычно Tab).
     *
     * @param reader {@link LineReader}, для которого выполняется автодополнение.
     * @param line текущая разбираемая (parsed) строка ввода, содержащая слово, для которого нужно найти дополнения.
     * @param candidates список, в который следует добавить объекты {@link Candidate}, представляющие возможные варианты автодополнения.
     */
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();
        commandManager.getCommands().keySet().stream()
                .filter(commandName -> commandName.startsWith(word))
                .sorted()
                .forEach(commandName -> candidates.add(new Candidate(commandName)));
    }
}