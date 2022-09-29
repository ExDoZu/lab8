package zuev.nikita.server.in_thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zuev.nikita.AuthorizationData;
import zuev.nikita.message.ClientRequest;
import zuev.nikita.message.ServerResponse;
import zuev.nikita.server.Hasher;
import zuev.nikita.server.ServerMain;
import zuev.nikita.server.net.Connection;
import zuev.nikita.server.net.SocketChannelIO;
import zuev.nikita.structure.Organization;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;


/**
 * Class that got connection with new client, authorizes it
 * and launches reader and handler of clients commands.
 */
public class ClientHandler implements Runnable {

    private final Map<String, Organization> collection;
    private final SocketChannel socketChannel;
    private final Statement statement;

    private final Logger log = LoggerFactory.getLogger(ServerMain.class);

    public ClientHandler(Map<String, Organization> collection, SocketChannel socketChannel, Statement statement) {
        this.collection = collection;
        this.socketChannel = socketChannel;
        this.statement = statement;
    }

    private void authorizeConnectedUser(SocketChannelIO socketChannelIO) {
        ClientRequest clientRequest;
        log.info("Authorization is begun.");
        try {
            clientRequest = socketChannelIO.read();
            String signMethod = clientRequest.getFullCommand()[0];
            AuthorizationData authorizationData = clientRequest.getAuthorizationData();
            ResultSet resultSet;
            log.info("Got authorization type: " + signMethod);
            String login = authorizationData.getLogin();
            String password = authorizationData.getPassword();

            String passwordHash = null;

            resultSet = statement.executeQuery("SELECT * FROM users WHERE login = '" + login + "';");
            boolean loginFound = false;
            while (resultSet.next()) {
                String databaseLogin = resultSet.getString("login");
                log.debug("Comparing of " + login + " and " + databaseLogin);
                if (databaseLogin.equals(login)) {
                    loginFound = true;
                    passwordHash = resultSet.getString("password");
                    break;
                }
            }

            if(signMethod.equals("login")){
                if(!loginFound){
                    log.info("Login not found");
                    socketChannelIO.write("Login not found", ServerResponse.NEW_USER, null);

                }else if(Hasher.getHashSHA384(password).equals(passwordHash)){
                    log.info("User " + login + " is successfully authorized");
                    log.debug("Password hash: " + passwordHash+"\nPassword: " + password+"\nHashed password: " + Hasher.getHashSHA384(password));
                    socketChannelIO.write("Login successful", ServerResponse.OK, null);
                }else{
                    log.info("Wrong password");
                    socketChannelIO.write("Wrong password", ServerResponse.WRONG_PASSWORD, null);
                }
            }
            else if(signMethod.equals("registration")){
                if(loginFound){
                    log.info("Login already exists");
                    socketChannelIO.write("Login already exists", ServerResponse.OLD_USER, null);

                } else{
                    statement.executeUpdate("INSERT INTO users VALUES ('" + login + "', '" + Hasher.getHashSHA384(password) + "')");
                    socketChannelIO.write("Registered", ServerResponse.OK, null);
                    log.info("User " + login + " is successfully registered");
                }
            }else{
                log.error("Wrong authorization type");
                socketChannelIO.write("Wrong authorization type", -1, null);
            }
        } catch (IOException | SQLException e) {
            log.error("Error while authorization", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

        Connection connection = new Connection(socketChannel, collection);
        authorizeConnectedUser(connection.getSocketChannelIO());

        boolean notGotExit = true;
        while (notGotExit) {
            try {
                notGotExit = connection.clientHandle();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
