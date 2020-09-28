package sample.Emisor;

import sample.Receptor.NetworkConnection;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkConnection {

    private String ip;
    private int port;

    /***
     * Si el isServer es false, entonces esta funcion se encarga de que el servidor use el puerto e IP previamente establecidos.
     * @param ip la ip que se utilizara.
     * @param port el puerto que se utilizara.
     * @param onReceiveCallback cumple el rol de "listener".
     */

    public Client(String ip, int port, Consumer<Serializable> onReceiveCallback){
        super(onReceiveCallback);
        this.ip = ip;
        this.port = port;
    }
    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
