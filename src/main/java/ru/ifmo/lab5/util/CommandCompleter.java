package ru.ifmo.lab5.util;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import ru.ifmo.lab5.managers.CommandManager;

import java.util.List;
import java.util.stream.Collectors;

public class CommandCompleter implements Completer {

    private final CommandManager commandManager;

    public CommandCompleter(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word(); // Текущее вводимое слово
        // Получаем актуальный список имен команд из CommandManager
        // и фильтруем те, которые начинаются с введенного слова
        commandManager.getCommands().keySet().stream()
                .filter(commandName -> commandName.startsWith(word))
                .sorted()
                .forEach(commandName -> candidates.add(new Candidate(commandName)));
    }
}