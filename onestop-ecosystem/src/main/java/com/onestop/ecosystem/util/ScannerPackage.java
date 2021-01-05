package com.onestop.ecosystem.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @description 用于扫描指定包下的所有类
 * @author chenq
 * @date 2019年9月2日17:59:25
 */
public class ScannerPackage {
    public static Set<Class<?>> getClasses(String pack) {
        // 第一个class类集合
        Set<Class<?>> classes = new LinkedHashSet<>(16);
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字，并进行替换
        String packageName = pack;
        String packageDirName = StringUtils.replace(pack, ".", "/");
        // 定义一个枚举的集合，并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if (StringUtils.equals(protocol, "file")) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if (StringUtils.equals(protocol, "jar")) {
                    // 如果是jar包文件，定义一个JarFile
                    JarFile jarFile;
                    try {
                        // 获取jar
                        jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包中得到一个枚举类
                        Enumeration<JarEntry> entries = jarFile.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体，可以是目录和一些jar包里的其他文件，如META-INF等文件
                            JarEntry jarEntry = entries.nextElement();
                            String name = jarEntry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (StringUtils.startsWith(name, packageDirName)) {
                                int index = name.lastIndexOf('/');
                                // 如果以、结尾是一个包
                                if (index != -1) {
                                    // 获取包名，把/替换成.
                                    packageName = name.substring(0, index).replace('/', '.');
                                }
                                // 如果可以迭代下去，并且是一个包
                                if ((index != -1) || recursive) {
                                    //如果是一个.class文件，而且不是目录
                                    if (StringUtils.endsWith(name, ".class") && !jarEntry.isDirectory()) {
                                        // 去掉后面的.class，获取真正类的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException enfe) {
                                            enfe.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
                                                  Set<Class<?>> classes) {
        // 获取此包的目录，建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在，就获取包下面的所有文件，包括目录
        File[] dirFiles = dir.listFiles(pathname -> {
            // 自定义过滤规则，如果可以循环或者是以.class结尾的文件
            return (recursive && pathname.isDirectory()) || (pathname.getName().endsWith(".class"));
        });
        // 循环所有文件
        for (File file: dirFiles) {
            // 如果是目录，则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + '.' + file.getName(), file.getAbsolutePath(),
                        recursive, classes);
            } else {
                // 如果是java类文件，去掉后面的.class，只保留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
