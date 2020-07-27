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

import java.util.List;

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

    /**
     * Fetch a list of players on the server used to display in the atom package.
     *
     * @return A list of players on the server, or null if there was an error.
     */
    List<ApiPlayer> getPlayerList();
}
