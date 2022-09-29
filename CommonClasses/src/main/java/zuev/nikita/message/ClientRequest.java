package zuev.nikita.message;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Organization;

import java.io.Serializable;
import java.util.Hashtable;

public class ClientRequest implements Serializable {
    private final String[] fullCommand;
    private final Organization organization;
    private final AuthorizationData authorizationData;

    private final Hashtable<String, Organization> collection;

    public ClientRequest(String[] fullCommand, Organization organization, AuthorizationData authorizationData, Hashtable<String, Organization> collection) {
        this.fullCommand = fullCommand;
        this.organization = organization;
        this.authorizationData=authorizationData;
        this.collection = collection;
    }

    public Organization getOrganization() {
        return organization;
    }

    public String[] getFullCommand() {
        return fullCommand;
    }

    public AuthorizationData getAuthorizationData() {
        return authorizationData;
    }
}
