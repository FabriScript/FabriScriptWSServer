package me.geek.tom.fabriscript.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FabriScriptWebSocketServer extends WebSocketServer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final IFabriScript fabriScript;

    public FabriScriptWebSocketServer(InetSocketAddress addr, IFabriScript fabriScript) {
        super(addr);
        this.fabriScript = fabriScript;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Got new connection from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Client from " + conn.getRemoteSocketAddress() + " disconnected with code : " + reason + " (" + code + ")");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        JsonObject obj = GSON.fromJson(message, JsonObject.class);
        String command = obj.get("command").getAsString();
        JsonObject content = obj.getAsJsonObject("content");

        switch (command) {
            case "sendScript":
                JsonArray data = content.getAsJsonArray("data");
                System.out.println("Received a script called '" + content.get("name").getAsString()
                        + "' with " + data.size() + " lines.");
                StringBuilder contentBuilder = new StringBuilder();
                for (int i = 0; i < data.size(); i++) {
                    contentBuilder.append(data.get(i).getAsString()).append("\n");
                }
                boolean res = fabriScript.receiveScript(content.get("name").getAsString(), contentBuilder.toString());

                conn.send(GSON.toJson(createSendReply(res)));
                break;
            case "runScript":
                String name = content.get("name").getAsString();
                System.out.println("Running script: " + name);
                conn.send(GSON.toJson(createRunReply(fabriScript.runScript(name))));
                break;
            default:
                conn.close();
                break;
        }
    }

    private JsonObject createSendReply(boolean res) {
        JsonObject obj = new JsonObject();
        obj.addProperty("command", "sendScript");
        obj.addProperty("success", res);
        return obj;
    }

    private JsonObject createRunReply(boolean res) {
        JsonObject obj = new JsonObject();
        obj.addProperty("command", "runScript");
        //obj.addProperty("result", (new Random().nextInt(10) > 5) ? "success" : "failed");
        obj.addProperty("result", res);
        return obj;
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Error occurred on connection from " + conn.getRemoteSocketAddress());
        ex.printStackTrace();
        conn.close();
    }

    @Override
    public void onStart() {
        System.out.println("Started FabriScript server on " + getAddress());
    }

    public static void main(String[] args) {
        FabriScriptWebSocketServer server = new FabriScriptWebSocketServer(new InetSocketAddress("localhost", 80),
                new TestFabriScriptImpl());
        server.start();
    }
}
