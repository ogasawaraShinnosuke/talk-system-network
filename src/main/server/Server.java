package main.server;


import main.server.logic.ServerLogic;

import java.io.IOException;

/**
 * Created by os on 2015/05/26.
 */
public class Server extends ServerLogic {
    public static void main(String[] args) throws IOException {
        new Server().generator(args);
    }

    private void generator(String[] args) throws IOException {
        run(args[0]);
    }

    // disable contsructor.
    private Server(){}
}
