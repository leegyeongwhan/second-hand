package com.secondhand.domain.chat.service;

import com.secondhand.domain.chat.ChatMessage;
import com.secondhand.domain.chat.ChatRoom;
import com.secondhand.domain.chat.dto.ChatroomDeatail;
import com.secondhand.domain.chat.dto.ChatroomList;
import com.secondhand.domain.chat.repository.ChatRoomRedisRepository;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.member.MemberRepository;
import com.secondhand.domain.product.Product;
import com.secondhand.service.ProductService;
import com.secondhand.util.MemberServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomFacadeService {

    private final ChannelTopic channelTopic;
    private final MemberRepository memberRepository;
    private final ProductService productService;
    private final ChatRoomService chatRoomService;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRedisRepository chatRoomRedisRepository;

    /**
     * destination정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    /**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessage chatMessage) {
        chatMessage.setUserCount(chatRoomRedisRepository.getUserCount(chatMessage.getRoomId()));
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    public ChatroomList findRoomList() {
        List<ChatRoom> chatRooms = chatRoomRedisRepository.findAllRoom();
        chatRooms.stream().forEach(room -> room.setUserCount(chatRoomRedisRepository.getUserCount(String.valueOf(room.getId()))));
        return new ChatroomList();
    }

    public ChatroomDeatail findRoomDetail() {
        return new ChatroomDeatail();
    }

    //TODO : 서비스 클래스 역할이 너무크다 분리시킬 필요가있다.
    @Transactional
    public long creatChatRoom(long productId, long userId) {
        Member buyer = MemberServiceUtils.findByMember(memberRepository, userId);
        Product product = productService.findById(productId);
        ChatRoom createdChatroom = chatRoomService.createChatRoom(product, buyer);
        return createdChatroom.getId();
    }
}
