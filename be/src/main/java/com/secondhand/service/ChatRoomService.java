package com.secondhand.service;

import com.secondhand.domain.chat.ChatRoom;
import com.secondhand.domain.chat.CustomSlice;
import com.secondhand.domain.chat.dto.ChatRoomResponse;
import com.secondhand.domain.chat.repository.ChatPaginationRepository;
import com.secondhand.domain.chat.repository.ChatRoomRepository;
import com.secondhand.domain.chat.service.ChatCountRepository;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.product.repository.ProductRepository;
import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomService {
    private static final long DEFAULT_MESSAGE_COUNT = 0L;

    private final ProductRepository productRepository;
    private final ChatPaginationRepository chatPaginationRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatCountRepository chatCountRepository;

    public CustomSlice<ChatRoomResponse> read(Long memberId, Pageable pageable, Long itemId) {
        Slice<ChatRoomResponse> response = chatPaginationRepository.findByMemberId(memberId, pageable, itemId);

        List<ChatRoomResponse> contents = response.getContent();

        Map<Long, Long> newMessageCounts = chatCountRepository.countNewMessage(memberId);

        contents.forEach(chatRoomResponse -> {
            Long chatRoomId = chatRoomResponse.getChatRoomId();
            Long messageCount = newMessageCounts.getOrDefault(chatRoomId, DEFAULT_MESSAGE_COUNT);
            chatRoomResponse.assignNewMessageCount(messageCount);
        });

        boolean hasNext = response.hasNext();
        Long nextCursor = hasNext ? (long) (pageable.getPageNumber() + 1) : null;

        return new CustomSlice<>(contents, nextCursor, response.hasNext());
    }

    @Transactional
    public Long createChatRoom(Long productId, Long senderId) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> NotFoundException.productNotFound(ErrorMessage.NOT_FOUND, productId));
            ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(senderId, productId, product.getMember().getId()));
            product.increaseChatCount();
            return chatRoom.getId();
        }

        public Long getReceiverId(Long chatRoomId) {
            ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        return chatRoom.getSeller().getId();
    }
}
