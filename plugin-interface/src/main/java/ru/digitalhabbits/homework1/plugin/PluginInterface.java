package ru.digitalhabbits.homework1.plugin;

import com.sun.source.tree.Tree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface PluginInterface {

    default TreeMap<String, Integer> getDictionary(String text){
        TreeMap<String, Integer> map = new TreeMap<>();
        text = text.replaceAll("\\\\n", "\n").toLowerCase();
        Pattern pattern = Pattern.compile("(\\b[a-zA-Z][a-zA-Z.0-9]*\\b)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            int i = 1;
            if (map.containsKey(matcher.group()))
                i = map.get(matcher.group()) + 1;
            map.put(matcher.group(), i);
        }
        return map;
    }
    @Nullable
    String apply(@Nonnull String text);
}
