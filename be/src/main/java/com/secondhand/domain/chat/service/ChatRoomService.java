//package com.secondhand.domain.chat.service;
//
//import com.secondhand.domain.chat.ChatRoom;
//import com.secondhand.domain.chat.repository.ChatRoomRepository;
//import com.secondhand.domain.member.Member;
//import com.secondhand.domain.product.Product;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class ChatRoomService {
//
//    private final ChatRoomRepository chatRoomRepository;
//    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
//    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
//    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장
//
//    @Resource(name = "redisTemplate")
//    private HashOperations<String, UUID, ChatRoom> hashOpsChatRoom;
//    @Resource(name = "redisTemplate")
//    private HashOperations<String, String, String> hashOpsEnterInfo;
//    @Resource(name = "redisTemplate")
//    private ValueOperations<String, String> valueOps;
//    private final ApplicationEventPublisher eventPublisher;
//
//    @Transactional
//    public ChatRoom createChatRoom(Product product, Member buyer) {
//        // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
////        if (chatRoomRepository.findByBuyer_IdAndItem_Id(buyer.getId(), item.getId()).isPresent()) {
////            throw new ExistChatRoomException("이미 존재하는 채팅방입니다.");
////        }
////        if (item.isSameSellerAndBuyer(buyer)) {
////            throw new BuyerException("구매자는 판매자와 같을 수 없습니다.");
////        }
//
//        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(product, buyer));
//        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getChatRoomId(), chatRoom);
//
//        //이벤트 pub/sub
//        eventPublisher.publishEvent(new ChatroomCreatedEvent(ChatroomInfo.from(chatRoom)));
//
//        return chatRoom;
//    }
//}
