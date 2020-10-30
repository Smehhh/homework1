package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Collectors;

public class FrequencyDictionaryPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        return getDictionary(text).entrySet().stream().map(entry -> entry.getKey() + " " + entry.getValue()).
                collect(Collectors.joining("\n"));
    }
}
