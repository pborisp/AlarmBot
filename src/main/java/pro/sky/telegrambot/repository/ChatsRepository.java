package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Chats;

public interface ChatsRepository extends JpaRepository<Chats, Long> {

}
