package org.fountainmc.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import org.fountainmc.WetServer;
import org.fountainmc.api.plugin.Plugin;
import org.fountainmc.api.plugin.PluginContainer;
import org.fountainmc.api.plugin.PluginManager;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class WetPluginManager implements PluginManager {

    ArrayList<PluginContainer> plugins;

    public WetPluginManager() {
        this.plugins = new ArrayList<PluginContainer>();
    }

    public void loadPlugins(File dir) {
        if (dir.isDirectory()) {
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    try {
                        URL url = file.toURI().toURL();
                        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                        method.setAccessible(true);
                        method.invoke(loader, url);
                        ArrayList<String> classes = getClasses(file);
                        JarFile jar = new JarFile(file);
                        for (String className : classes) {
                            JarEntry entry = jar.getJarEntry(className);
                            String m = className.replaceAll("/", "\\.");
                            String clazz = m.substring(0, m.length() - 6);
                            InputStream in = jar.getInputStream(entry);
                            ClassReader cr = new ClassReader(in);
                            cr.accept(new AnnotationScanner(this, loader, clazz, cr), 0);
                        }
                        jar.close();
                    } catch (IOException | NoSuchMethodException | SecurityException | InvocationTargetException
                            | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void loadPlugin(ClassLoader loader, String className, ClassReader classReader) {
        try {
            Class<?> clazz = loader.loadClass(className);
            final Plugin anno = clazz.getAnnotation(Plugin.class);
            System.out.println((anno.name().isEmpty() ? anno.id() : anno.name()) + " has been loaded.");
            final Object plugin = clazz.newInstance();
            PluginContainer container = new PluginContainer() {

                @Override
                public String getId() {
                    return anno.id();
                }

                @Override
                public String getName() {
                    return anno.name();
                }

                @Override
                public String getVersion() {
                    return anno.version();
                }

                @Override
                public String getDescription() {
                    return anno.description();
                }

                @Override
                public String getUrl() {
                    return anno.url();
                }

                @Override
                public String[] getAuthor() {
                    return anno.author();
                }

                @Override
                public Object getInstance() {
                    return plugin;
                }

            };
            plugins.add(container);
            WetServer.getInstance().getEventManager().registerListener(plugin);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getClasses(File jar) {
        ArrayList<String> classes = new ArrayList<String>();
        try {
            JarInputStream jarStream = new JarInputStream(new FileInputStream(jar));
            JarEntry jarEntry;
            while (true) {
                jarEntry = jarStream.getNextJarEntry();
                if (jarEntry == null)
                    break;
                if (jarEntry.getName().endsWith(".class")) {
                    classes.add(jarEntry.getName());
                }
            }
            jarStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public Collection<PluginContainer> getPlugins() {
        return plugins;
    }

    class AnnotationScanner extends ClassVisitor {

        private WetPluginManager pluginManager;
        private ClassLoader loader;
        private String className;
        private ClassReader classReader;

        private AnnotationScanner(WetPluginManager pluginManager, ClassLoader loader, String className,
                ClassReader classReader) {
            super(Opcodes.ASM4);
            this.pluginManager = pluginManager;
            this.loader = loader;
            this.className = className;
            this.classReader = classReader;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (desc.contains(Type.getInternalName(Plugin.class))) {
                pluginManager.loadPlugin(loader, className, classReader);
            }
            return super.visitAnnotation(desc, visible);
        }
    }

    @Override
    public PluginContainer fromInstance(Object instance) {
        for (PluginContainer container : plugins) {
            if (container.getInstance().equals(instance)) {
                return container;
            }
        }
        return null;
    }

    @Override
    public PluginContainer getPlugin(String id) {
        for (PluginContainer container : plugins) {
            if (container.getId().equals(id)) {
                return container;
            }
        }
        return null;
    }

    @Override
    public boolean isLoaded(String id) {
        for (PluginContainer container : plugins) {
            if (container.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

}
