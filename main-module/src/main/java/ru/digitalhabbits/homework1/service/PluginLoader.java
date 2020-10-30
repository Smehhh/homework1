package ru.digitalhabbits.homework1.service;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.Reflection;
import org.slf4j.Logger;
import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

public class PluginLoader {
    private static final Logger logger = getLogger(PluginLoader.class);
    private static final String PLUGIN_EXT = "jar";
    private static final String PACKAGE_TO_SCAN = "ru.digitalhabbits.homework1.plugin";

    @Nonnull
    public List<Class<? extends PluginInterface>> loadPlugins(@Nonnull String pluginDirName) {
        List<Class<? extends PluginInterface>> classes = new ArrayList<>();
        List<String> jarList = new ArrayList<>();
        try {
            jarList = Arrays.stream(new File(pluginDirName).listFiles()).map(File::getAbsolutePath).filter(s -> s.endsWith(PLUGIN_EXT)).collect(Collectors.toList());
        }catch (NullPointerException e){
            logger.error("No plugin jar files");
            System.exit(0);
        }

        for (String jarName : jarList) {
            try {
                File pathToJar = new File(jarName);
                JarFile jarFile = new JarFile(pathToJar);
                Enumeration<JarEntry> entries = jarFile.entries();
                URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
                URLClassLoader cl = URLClassLoader.newInstance(urls);
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if(entry.isDirectory() || !entry.getName().endsWith(".class")){
                        continue;
                    }
                    String className = entry.getName().substring(0,entry.getName().length()-6);
                    className = className.replace('/', '.');
                    Class<?> c = cl.loadClass(className);
                    if (Arrays.asList(c.getInterfaces()).contains(PluginInterface.class)) {
                        classes.add((Class<? extends PluginInterface>) c);
                    }
                }
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        return classes;
    }
}
