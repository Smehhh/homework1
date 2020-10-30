package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;



public class CounterPlugin implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        int nCount = text.split("\n").length;
        int wCount = getDictionary(text).values().stream().mapToInt(x -> x).sum();
        long lCount = text.length();
        /*
            or if we need only letters
            long lCount =  text.chars().mapToObj(c -> (char) c).filter(Character::isLetter).count();
        */
        return nCount + ";" + wCount + ";" + lCount;
    }
}
