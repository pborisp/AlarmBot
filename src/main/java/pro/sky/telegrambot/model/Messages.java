package pro.sky.telegrambot.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(appliesTo = "messages")
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chats_id")
    @JsonBackReference
    private Chats chats;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Chats getChats() {
        return chats;
    }

    public void setChats(Chats chats) {
        this.chats = chats;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Messages messages = (Messages) o;
        return Objects.equals(id, messages.id) && Objects.equals(text, messages.text) &&
                Objects.equals(dateTime, messages.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, dateTime);
    }
}
