package com.secondhand.application.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.secondhand.application.ApplicationTestSupport;
import com.secondhand.config.FixtureFactory;
import com.secondhand.domain.chat.ChatLog;
import com.secondhand.domain.chat.ChatRoom;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.member.MemberProfile;
import com.secondhand.domain.product.Product;
import com.secondhand.service.ChatLogService;
import com.secondhand.web.dto.chat.ChatLogResponse;
import java.util.List;
import java.util.Optional;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("채팅로그서비스 테스트")
public class ChatLogServiceTest extends ApplicationTestSupport {

    @Autowired
    private ChatLogService chatLogService;

    @DisplayName("채팅방 아이디와 몇 번째까지 읽었는지에 대한 정보가 주어지면 채팅 내역 조회에 성공한다.")
    @Test
    void givenChatRoomIdAndMessageIndex_whenGetMessages_thenSuccess() {
        // given
        MemberProfile profile = supportRepository.save(MemberProfile.builder()
                .memberEmail("gamja@naver.com")
                .build());
        Member buyer = supportRepository.save(FixtureFactory.createMember());

        Member seller = supportRepository.save(Member.builder()
                .loginName("gamja")
                .imgUrl("url")
                .oauthProvider("KAKAO")
                .memberProfile(profile)
                .build());

        ChatRoom chatRoom = prepareToChatAndReturnChatRoom(buyer, seller);

        for (int i = 1; i <= 10; i++) {
            supportRepository.save(ChatLog.builder()
                    .chatRoom(chatRoom)
                    .senderId(buyer.getId())
                    .readCount(1)
                    .message("안녕하세용" + i)
                    .build());
        }

        // when
        ChatLogResponse response = chatLogService.getMessages(chatRoom.getId(), 0, buyer.getId());

        // then
        List<ChatLog> chatLogs = supportRepository.findAll(ChatLog.class);
        assertAll(
                () -> assertThat(response.getChat()).hasSize(10),
                () -> assertThat(response.getChatPartnerName()).isEqualTo("gamja"),
                () -> assertThat(response.getItem().getTitle()).isEqualTo("선풍기"),
                () -> assertThat(chatLogs.get(9).getReadCount()).isEqualTo(1)
        );
    }

    @DisplayName("채팅을 전송할 때 채팅 내용이 주어지면 전송에 성공한다.")
    @Test
    void givenMessage_whenSendMessage_thenSuccess() {
        // given
        MemberProfile profile = supportRepository.save(MemberProfile.builder()
                .memberEmail("gamja@naver.com")
                .build());
        Member buyer = supportRepository.save(FixtureFactory.createMember());

        Member seller = supportRepository.save(Member.builder()
                .loginName("gamja")
                .imgUrl("url")
                .oauthProvider("KAKAO")
                .memberProfile(profile)
                .build());


        ChatRoom chatRoom = prepareToChatAndReturnChatRoom(buyer, seller);

        // when
        chatLogService.sendMessage("선풍기 사려 그러는데요!", chatRoom.getId(), buyer.getId());
        chatLogService.sendMessage("혹시 할인 되나요..?", chatRoom.getId(), buyer.getId());

        // then
        List<ChatLog> chatLogs = supportRepository.findAll(ChatLog.class);

        assertThat(chatLogs).hasSize(2);
    }

    private ChatRoom prepareToChatAndReturnChatRoom(Member buyer, Member seller) {
        Product product = supportRepository.save(FixtureFactory.createProduct("선풍기", "가전", seller));
        return supportRepository.save(ChatRoom.builder()
                .product(product)
                .subject("")
                .seller(seller)
                .buyer(buyer)
                .build());
    }
}
