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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.function.Consumer;

public class CommandHelper {

    private final IFabriScript fabriScript;

    public CommandHelper(IFabriScript fabriScript) {
        this.fabriScript = fabriScript;
    }

    public void handleCommand(String command, JsonObject content, Consumer<JsonObject> sender, Runnable closer) {
        switch (command) {
            case "sendScript":
                handleSendScript(content, sender);
                break;
            case "runScript":
                handleRunScript(content, sender);
                break;
            case "listPlayers":
                handleListPlayers(content, sender);
                break;
            default:
                closer.run();
                break;
        }
    }

    private void handleListPlayers(JsonObject content, Consumer<JsonObject> sender) {
        List<ApiPlayer> players = fabriScript.getPlayerList();
        JsonObject res = new JsonObject();
        res.addProperty("command", "listPlayers");
        res.addProperty("result", (players != null) ? "success" : "failed");
        if (players != null) {
            JsonArray playerArray = new JsonArray();
            players.stream().map(this::playerToJson).forEach(playerArray::add);
            res.add("players", playerArray);
        }
        sender.accept(res);
    }

    private JsonObject playerToJson(ApiPlayer player) {
        JsonObject object = new JsonObject();
        object.addProperty("name", player.getName());
        object.addProperty("uuid", player.getUuid().toString());
        return object;
    }

    private void handleRunScript(JsonObject content, Consumer<JsonObject> sender) {
        String scriptName = content.get("name").getAsString();
        boolean success = fabriScript.runScript(scriptName);
        JsonObject res = new JsonObject();
        res.addProperty("result", success ? "success" : "failed");
        res.addProperty("command", "runScript");
        sender.accept(res);
    }

    private void handleSendScript(JsonObject content, Consumer<JsonObject> sender) {
        String scriptName = content.get("name").getAsString();
        String script = stitchScript(content.get("data").getAsJsonArray());
        boolean success = fabriScript.receiveScript(scriptName, script);
        JsonObject res = new JsonObject();
        res.addProperty("result", success ? "success" : "failed");
        res.addProperty("command", "sendScript");
        sender.accept(res);
    }

    private String stitchScript(JsonArray data) {
        StringBuilder builder = new StringBuilder();
        data.forEach(s -> builder.append(s.getAsString()).append("\n"));
        return builder.toString();
    }
}
