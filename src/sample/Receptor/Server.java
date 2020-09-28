package sample.Receptor;

import sample.Receptor.NetworkConnection;

import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends NetworkConnection {

    private int port;

    /***
     * Si el isServer es true entonces esta funcion se encarga de que el servidor use el puerto previamente establecido.
     * @param port el puerto que utilizara.
     * @param onReceiveCallback cumple el rol de "listener".
     */

    public Server(int port, Consumer<Serializable> onReceiveCallback) {
        super(onReceiveCallback);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }


    @Override
    protected String getIP() {
        return null;
    }


    @Override
    protected int getPort() {
        return port;
    }
}
