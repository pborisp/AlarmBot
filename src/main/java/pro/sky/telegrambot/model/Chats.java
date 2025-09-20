package pro.sky.telegrambot.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Table;

import java.util.List;
import java.util.Objects;

@Entity
@Table(appliesTo = "chats")
public class Chats {
    @Id
    private Long id;

    private String userName;
    private String firstName;

    @OneToMany(mappedBy = "chats")
    @JsonManagedReference
    @JsonIgnore
    private List<Messages> messages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Chats{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Chats chats = (Chats) o;
        return Objects.equals(id, chats.id) && Objects.equals(userName, chats.userName) && Objects.equals(firstName, chats.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, firstName);
    }
}
