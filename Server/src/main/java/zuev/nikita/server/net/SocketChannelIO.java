package zuev.nikita.server.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zuev.nikita.message.ClientRequest;
import zuev.nikita.message.ServerResponse;
import zuev.nikita.structure.Organization;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Map;

public class SocketChannelIO {
    private final static Logger log = LoggerFactory.getLogger(SocketChannelIO.class);
    private final SocketChannel socketChannnel;


    public SocketChannelIO(SocketChannel socketChannel) throws IOException {
        socketChannel.configureBlocking(true);
        this.socketChannnel = socketChannel;
    }

    public ClientRequest read() throws IOException, ClassNotFoundException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
        int gotBytes = socketChannnel.read(byteBuffer);
        log.debug("data got "+ gotBytes +" bytes");

        if (gotBytes == -1) return new ClientRequest(new String[]{"exit"}, null, null, null);
        else if (gotBytes == 0) return null;
        else {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteBuffer.array());
            ObjectInputStream objectInputStream = new ObjectInputStream(bis);
            ClientRequest clientRequest = (ClientRequest) objectInputStream.readObject();
            objectInputStream.close();
            bis.close();
            log.info("Data is successfully got");
            return clientRequest;
        }
    }

    public void write(String response, int statusCode, Map<String, Organization> collection) throws IOException {
        log.debug("socketChannelIO write");
        ServerResponse serverResponse = new ServerResponse(response, statusCode, collection);
        log.debug("Server response is created");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        log.debug("Byte array output stream is created");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos);
        log.debug("Sending response to client");
        objectOutputStream.writeObject(serverResponse);
        objectOutputStream.flush();
        log.debug("Response is serialized");
        ByteBuffer byteBuffer = ByteBuffer.wrap(bos.toByteArray());
        socketChannnel.write(byteBuffer);
        objectOutputStream.flush();
        log.debug("Response is sent");
        log.info("Data is successfully sent");
        objectOutputStream.close();
    }

    public void block() throws IOException {
        socketChannnel.configureBlocking(true);
    }

    public void unblock() throws IOException {
        socketChannnel.configureBlocking(false);
    }
}
