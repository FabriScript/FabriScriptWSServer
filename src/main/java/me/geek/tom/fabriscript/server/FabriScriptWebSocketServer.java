package me.geek.tom.fabriscript.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class FabriScriptWebSocketServer extends WebSocketServer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final IFabriScript fabriScript;
    private final CommandHelper commandHelper;

    public FabriScriptWebSocketServer(InetSocketAddress addr, IFabriScript fabriScript) {
        super(addr);
        this.fabriScript = fabriScript;
        this.commandHelper = new CommandHelper(this.fabriScript);
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
        this.commandHelper.handleCommand(command, content, res ->
                conn.send(GSON.toJson(res)), conn::close);
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
