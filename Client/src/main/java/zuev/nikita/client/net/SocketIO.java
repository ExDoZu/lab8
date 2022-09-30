package zuev.nikita.client.net;

import zuev.nikita.AuthorizationData;
import zuev.nikita.message.ClientRequest;
import zuev.nikita.message.ServerResponse;
import zuev.nikita.structure.Organization;

import java.io.*;
import java.util.Hashtable;

/**
 * Instrument to send and get serialized objects using socket
 */
public class SocketIO {

    static private Connection connection;

    public static void setConnection(Connection newConnection){
        connection = newConnection;
    };


    static public void write(String[] fullCommand, Organization organization, AuthorizationData authorizationData, Hashtable<String, Organization> collection) throws IOException {
        ClientRequest clientRequest = new ClientRequest(fullCommand, organization, authorizationData, collection);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream objOutputStream = new ObjectOutputStream(bos);
        objOutputStream.writeObject(clientRequest);
        objOutputStream.flush();
        connection.getSocket().getOutputStream().write(bos.toByteArray());
        connection.getSocket().getOutputStream().flush();
        objOutputStream.close();
        bos.close();
    }

    static public ServerResponse read() throws IOException, ClassNotFoundException {
        byte[] arr = new byte[2*16384];
        //Magic bytes for ObjectInputStream. They are unnecessary to initialize ObjectInputStream
        arr[0] = -84;
        arr[1] = -19;
        arr[2] = 0;
        arr[3] = 5;
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        ObjectInputStream objInputStream = new ObjectInputStream(bis);
        connection.getSocket().getInputStream().read(arr);
        ServerResponse serverResponse = (ServerResponse) objInputStream.readObject();
        objInputStream.close();
        bis.close();
        return serverResponse;
    }
}
