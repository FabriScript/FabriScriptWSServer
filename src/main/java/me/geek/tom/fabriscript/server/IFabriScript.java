package me.geek.tom.fabriscript.server;

/**
 * Interface to communicate with the server-side instance of the mod.
 */
public interface IFabriScript {

    /**
     * Called when the server receives a script to be saved.
     *
     * @param name The name of the script
     * @param content The string content of the file.
     * @return true if the script was accepted, false otherwise.
     */
    boolean receiveScript(String name, String content);

    /**
     * Executes a script with the given name.
     *
     * @param name The name of the script to run
     * @return true if the script was executed successfully.
     */
    boolean runScript(String name);
}
