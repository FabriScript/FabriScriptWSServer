package me.geek.tom.fabriscript.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class TestFabriScriptImpl implements IFabriScript {
    @Override
    public boolean receiveScript(String name, String content) {
        File file = new File(name);
        if (!file.exists()) {
            try {
                if (!file.createNewFile())
                    return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(file))) {
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean runScript(String name) {
        return new File(name).exists();
    }
}
