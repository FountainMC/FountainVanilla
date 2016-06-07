package xyz.jadonfowler.fountain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import xyz.jadonfowler.fountain.api.plugin.Plugin;

public class PluginManager {

    ArrayList<Object> plugins = new ArrayList<Object>();

    public void loadPlugins(File dir) {
        if (dir.isDirectory()) {
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    try {
                        ClassLoader loader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
                        ArrayList<String> classes = getClasses(file);
                        for (String className : classes) {
                            FileInputStream in = new FileInputStream(new File(className));
                            ClassReader cr = new ClassReader(in);
                            cr.accept(new AnnotationScanner(this, loader, className), 0);
                        }
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void loadPlugin(ClassLoader loader, String className) {
        try {
            plugins.add(loader.loadClass(className).newInstance());
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
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
                if (jarEntry == null) break;
                if (jarEntry.getName().endsWith(".class")) {
                    classes.add(jarEntry.getName().replaceAll("/", "\\."));
                }
            }
            jarStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    class AnnotationScanner extends ClassVisitor {

        private PluginManager pluginManager;
        private ClassLoader loader;
        private String className;

        public AnnotationScanner(PluginManager pluginManager, ClassLoader loader, String className) {
            super(Opcodes.ASM4);
            this.pluginManager = pluginManager;
            this.loader = loader;
            this.className = className;
        }

        @Override public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (desc.equals(Type.getInternalName(Plugin.class))) {
                pluginManager.loadPlugin(loader, className);
            }
            return super.visitAnnotation(desc, visible);
        }
    }
}
