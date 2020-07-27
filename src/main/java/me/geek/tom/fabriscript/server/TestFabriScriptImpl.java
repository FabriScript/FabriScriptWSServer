/*
 * FabriScriptWSServer
 * Copyright (C) 2020 Tom_The_Geek
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package me.geek.tom.fabriscript.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    @Override
    public List<ApiPlayer> getPlayerList() {
        return Collections.singletonList(new ApiPlayer(UUID.randomUUID(), "Notch"));
    }
}
