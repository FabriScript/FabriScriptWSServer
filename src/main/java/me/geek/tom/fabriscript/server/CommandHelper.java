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
        data.forEach(s -> builder.append(s).append("\n"));
        return builder.toString();
    }
}
