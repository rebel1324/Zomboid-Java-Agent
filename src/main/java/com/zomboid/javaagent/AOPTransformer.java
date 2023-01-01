package com.zomboid.javaagent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.HashSet;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

public class AOPTransformer implements ClassFileTransformer {
    private final String className;
    private final OutputStream stream;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.stream.close();
    }

    public AOPTransformer(String className) throws IOException {
        this.className = className;
        Path hi = Paths.get("./bitchass.log");
        this.stream = Files.newOutputStream(hi);
    }

    public static HashSet<String> fuck = new HashSet<String>() {
        {
            add("zombie/Lua/Event");
            add("zombie/Lua/Event$Add");
            add("zombie/Lua/Event$Remove");
            add("zombie/Lua/KahluaNumberConverter");
            add("zombie/Lua/LuaEventManager");
            add("zombie/Lua/LuaHookManager");
            add("zombie/Lua/LuaManager");
            add("zombie/Lua/LuaManager/Exposer");
            add("zombie/Lua/MapObjects");
        }
    };

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className == null) {
            return null;
        }

        if (fuck.contains(className)) {
            System.out.println(className);
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(loader));
            classPool.appendSystemPath();

            try {
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod[] declaredMethods = ctClass.getDeclaredMethods();

                for (CtMethod declaredMethod : declaredMethods) {
                    this.stream.write(className.getBytes());
                    this.stream.write(":".getBytes());
                    this.stream.write(declaredMethod.getName().getBytes());
                    this.stream.write("=".getBytes());
                    this.stream.write(( className.equals("zombie/Lua/LuaManager") ? "ISLM" : "NONLM").getBytes());
                    this.stream.write(( declaredMethod.getName().equals("init") ? "WIW" : "WOW").getBytes());
                    this.stream.write("\n".getBytes());

                    if ( className.equals("zombie/Lua/LuaManager") && declaredMethod.getName().equals("init") ) {
                        this.stream.write("INSERT \n".getBytes());
                    }
                }
                return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        return classfileBuffer;
    }
}
