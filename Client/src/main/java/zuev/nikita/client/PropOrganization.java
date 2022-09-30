package zuev.nikita.client;


import javafx.beans.property.*;
import zuev.nikita.structure.Address;
import zuev.nikita.structure.Coordinates;
import zuev.nikita.structure.Organization;
import zuev.nikita.structure.OrganizationType;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class PropOrganization implements Comparable<PropOrganization> {
    private ResourceBundle resourceBundle;
    private StringProperty key;
    private IntegerProperty id;
    private StringProperty author;
    private StringProperty name;
    private LongProperty x;
    private DoubleProperty y;
    private StringProperty creationDate;
    private DoubleProperty annualTurnover;
    private StringProperty organizationType;
    private StringProperty postalAddress;

    private Date creationDateAsDate;
    private OrganizationType organizationTypeAsEnum;

    public PropOrganization(String key, Integer id, String author, String name, Long x, Double y, Date creationDate, Double annualTurnover, OrganizationType organizationType, Address postalAddress, ResourceBundle resourceBundle) {
        this.key = new SimpleStringProperty(key);
        this.id = new SimpleIntegerProperty(id);
        this.author = new SimpleStringProperty(author);
        this.name = new SimpleStringProperty(name);
        this.x = new SimpleLongProperty(x);
        this.y = new SimpleDoubleProperty(y);

        this.creationDateAsDate = creationDate;
        this.creationDate = new SimpleStringProperty(DateFormat.getDateInstance(DateFormat.DEFAULT, resourceBundle.getLocale()).format(creationDate));

        this.annualTurnover = new SimpleDoubleProperty(annualTurnover);

        this.organizationType = new SimpleStringProperty(resourceBundle.getString(organizationType.toString()));
        this.organizationTypeAsEnum = organizationType;

        this.postalAddress = new SimpleStringProperty(postalAddress.getZipCode());
        this.resourceBundle = resourceBundle;


    }

    public String getKey() {
        return key.get();
    }

    public StringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }
    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public long getX() {
        return x.get();
    }

    public LongProperty xProperty() {
        return x;
    }

    public void setX(long x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public Date getCreationDate() {
        return creationDateAsDate;
    }

    public StringProperty creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDateAsDate = creationDate;

        this.creationDate.set(DateFormat.getDateInstance(DateFormat.SHORT, resourceBundle.getLocale()).format(creationDate));
    }

    public double getAnnualTurnover() {
        return annualTurnover.get();
    }

    public DoubleProperty annualTurnoverProperty() {
        return annualTurnover;
    }

    public void setAnnualTurnover(double annualTurnover) {
        this.annualTurnover.set(annualTurnover);
    }

    public String getOrganizationType() {
        return organizationType.get();
    }

    public OrganizationType getOrganizationTypeAsEnum() {
        return organizationTypeAsEnum;
    }

    public StringProperty organizationTypeProperty() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType.set(organizationType.toString());
        this.organizationTypeAsEnum = organizationType;
    }

    public String getPostalAddress() {
        return postalAddress.get();
    }

    public StringProperty postalAddressProperty() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress.set(postalAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropOrganization that = (PropOrganization) o;

        if (id.get() != that.id.get()) return false;
        if (x.get() != that.x.get()) return false;
        if (that.y.get()!=y.get()) return false;
        if (that.annualTurnover.get() != annualTurnover.get()) return false;
        if (!getKey().equals(that.getKey())) return false;
        if (!getAuthor().equals(that.getAuthor())) return false;
        if (!getName().equals(that.getName())) return false;
        if (creationDateAsDate.getTime()!=that.creationDateAsDate.getTime()) return false;
        if (organizationTypeAsEnum!=that.organizationTypeAsEnum) return false;
        if(!resourceBundle.getLocale().equals(that.resourceBundle.getLocale())) return false;
        return getPostalAddress().equals(that.getPostalAddress());
    }

    @Override
    public int compareTo(PropOrganization o) {
        int compare = getName().compareTo(o.getName());
        if (compare == 0) {
            compare = new Coordinates(getX(), getY()).compareTo(new Coordinates(o.getX(), o.getY()));
            if (compare == 0) {
                compare = Double.compare(getAnnualTurnover(), o.getAnnualTurnover());
                if (compare == 0) {
                    compare = getPostalAddress().compareTo(o.getPostalAddress());
                    if (compare == 0) {
                        compare = getOrganizationTypeAsEnum().compareTo(o.getOrganizationTypeAsEnum());
                    }
                }
            }
        }
        return compare;
    }
}
