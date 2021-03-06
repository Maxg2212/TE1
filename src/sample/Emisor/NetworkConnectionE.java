package sample.Emisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnectionE {

    private static Logger log = LoggerFactory.getLogger(MainE.class);

    private ConnectionThread connThread = new ConnectionThread();
    private Consumer<Serializable> onReceiveCallback;

    /***
     * Esta funcion permite e inicia la conexion entre threads.
     * @param onReceiveCallback cumple el rol de "listener".
     */

    public NetworkConnectionE(Consumer<Serializable> onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
        connThread.setDaemon(true);
    }

    /***
     * Esta funcion inicia el thread para correr el programa, ademas que predice posibles errores gracias al exception.
     * @throws Exception se predicen posibles errores no contemplados a la hora de programar.
     */

    public void startConnection() throws Exception {
        try {
            connThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Esta funcion permite que se envíen y reciban arrays de bytes gracias al serializable.
     * @param data los mensajes que se enviaran.
     * @throws Exception se predicen posibles errores no contemplados a la hora de programar.
     */

    public void send(Serializable data) throws Exception {
        connThread.out.writeObject(data);

    }

    /***
     * Esta funcion es la encargada de terminar la conexion entre los Threads y así se termine la comunicacion.
     * @throws Exception se predicen posibles errores no contemplados a la hora de programar.
     */

    public void closeConnection()throws Exception {
        connThread.socket.close();

    }

    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();

    private class ConnectionThread extends Thread{
        private Socket socket;
        private ObjectOutputStream out;

        /***
         *Funcion que permite recibir y enviar mensajes porque es la usa el IP(127.0.0.1) y el puerto que se usa
         * para establecer una comunicacion directa entre ambos chats, y si resulta que es la condicion es verdadera
         * permite enviar y recibir datos(mensajes). Una vez que se cierra una ventana, comunica que la comunicacion se termino.
         * No posee ningun parametro.
         */

        @Override
        public void run() {
            try(ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
                Socket socket = isServer() ? server.accept() : new Socket(getIP(),getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while (true) {
                    Serializable data = (Serializable) in.readObject();
                    onReceiveCallback.accept(data);
                }
            }
            catch (Exception e) {
                log.info(e.getMessage(), e); // log de info
                onReceiveCallback.accept("Connection closed");

            }
        }
    }
}
