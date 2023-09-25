package com.secondhand.domain.chat.repository;

import com.secondhand.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatRoom, Long> {
}
