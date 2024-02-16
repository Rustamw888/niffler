package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.db.entity.auth.UserEntity;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.db.entity.userdata.FriendState;

import guru.qa.niffler.jupiter.annotation.old.User;
import guru.qa.niffler.jupiter.annotation.old.User.UserType;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User9Json {

    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String username;

    private transient String password;

    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("friendState")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FriendState friendState;

    transient User.UserType userType;
    transient List<User9Json> friends;
    transient List<User9Json> incomeInvitations;
    transient List<User9Json> outcomeInvitations;

    public User9Json() {
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<User9Json> getFriends() {
        return friends;
    }

    public void setFriends(List<User9Json> friends) {
        this.friends = friends;
    }

    public List<User9Json> getIncomeInvitations() {
        return incomeInvitations;
    }

    public void setIncomeInvitations(List<User9Json> incomeInvitations) {
        this.incomeInvitations = incomeInvitations;
    }

    public List<User9Json> getOutcomeInvitations() {
        return outcomeInvitations;
    }

    public void setOutcomeInvitations(List<User9Json> outcomeInvitations) {
        this.outcomeInvitations = outcomeInvitations;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public FriendState getFriendState() {
        return friendState;
    }

    public void setFriendState(FriendState friendState) {
        this.friendState = friendState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User9Json user9Json = (User9Json) o;
        return Objects.equals(id, user9Json.id) && Objects.equals(username, user9Json.username) && Objects.equals(firstname, user9Json.firstname) && Objects.equals(surname, user9Json.surname) && currency == user9Json.currency && Objects.equals(photo, user9Json.photo) && friendState == user9Json.friendState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, surname, currency, photo, friendState);
    }

    public static User9Json fromEntity(UserEntity entity) {
        User9Json usr = new User9Json();
        usr.setId(entity.getId());
        usr.setUsername(entity.getUsername());
        return usr;
    }
}
