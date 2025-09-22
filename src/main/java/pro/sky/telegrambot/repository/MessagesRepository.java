package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.Messages;

import java.util.Collection;

public interface MessagesRepository extends JpaRepository<Messages, Long> {

    @Query(value = "SELECT messages.id FROM messages m WHERE m.chats_id = chatId", nativeQuery = true)
    Long getIdMessagesByChatId(Long chatId);



}
