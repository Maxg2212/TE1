package sample.Emisor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.Receptor.NetworkConnection;
import sample.Receptor.Server;
//Se debe permitir el parallel run del main para que funcione.


public class MainE extends Application {

    private static Logger log = LoggerFactory.getLogger(MainE.class);

    private boolean isServer = false; //True para abrir Server y false para abrir Client

    private TextArea messages = new TextArea();
    private NetworkConnection connection = isServer ? createServer() : createClient();
    /***
     *Esta funcion es encargada de la interfaz(ventana) que se despliega cuando se corre el main y muestra mensaje
     * "failed to send" en caso que las dos ventanas no esten corriendo simultamente.
     * @return ventana(interfaz)
     */
    private Parent createContent() {
        messages.setPrefHeight(750);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = isServer ? "Persona1: " : "Persona2: ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");

            try {
                log.debug("Enviando mensaje..");

                connection.send(message);
                log.debug("Mensaje enviado..");
            }
            catch (Exception e) {
                log.error(e.getMessage(), e);   // log de error
                messages.appendText("Failed to send\n");

            }
        });

        VBox root = new VBox(40, messages, input);
        root.setPrefSize(500,500);
        return root;
    }
    /***
     *Esta funcion es la encargada de predecir errores que no están contemplados en el codigo, es una forma de asegurar
     * el buen funcionamiento del programa.
     * @throws Exception se predicen posibles errores no contemplados a la hora de programar.
     */
    @Override
    public void init() throws Exception {
        connection.startConnection();
    }
    /***
     * Muestra la ventana(chat) con el nombre "Chat Emisor/Receptor".
     * @param primaryStage se usara para desplegar ventanas.
     * @throws Exception se predicen posibles errores no contemplados a la hora de programar.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Chat Emisor/Receptor");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }
    /***
     * El proceso se detiene en caso que se cierre la conexion.
     * @throws Exception predice posibles errores que no fueron contemplados a la hora de programar.
     */
    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }
    /***
     * Funcion encargada de crear el chat del receptor, usa el puerto 55555 y en este sera el cual donde se enviaran mensajes.
     * @return Ventana del chat del receptor.
     */
    private Server createServer() {
        return new Server(55555, data -> {
            Platform.runLater(() -> {
               messages.appendText(data.toString() + "\n");
            });
        });
    }
    /***
     * Esta funcion crea el chat del emisor, implementa el puerto 55555 y la IP deseada, ambos seran de ayuda para
     * establecer como un puente para la comunicacion entre emisor y receptor.
     * @return Ventana del chat del emisor.
     */
    private Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }


    public static void main(String[] args) {

        launch(args);
    }
}
