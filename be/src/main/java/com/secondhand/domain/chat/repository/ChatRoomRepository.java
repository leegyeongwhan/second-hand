package com.secondhand.domain.chat.repository;

import com.secondhand.domain.chat.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT chatRoom.id FROM ChatRoom chatRoom WHERE chatRoom.product.id = :productId AND chatRoom.buyer.id = :buyerId")
    Optional<Long> findByProduct_IdAndBuyer_Id(@Param("productId") Long itemId,  @Param("buyerId") Long buyerId);
}
