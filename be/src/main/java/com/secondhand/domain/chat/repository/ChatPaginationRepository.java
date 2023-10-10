package com.secondhand.domain.chat.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.secondhand.domain.chat.dto.ChatRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.secondhand.domain.chat.QChatRoom.chatRoom;

@RequiredArgsConstructor
@Repository
public class ChatPaginationRepository implements PaginationRepository {
    private final JPAQueryFactory queryFactory;

    public Slice<ChatRoomResponse> findByMemberId(Long memberId, Pageable pageable, Long itemId) {
        Expression<String> loginIdExpression = createPartnerNameExpression(memberId);
        Expression<String> profileExpression = createPartnerProfileExpression(memberId);

        List<ChatRoomResponse> chatRoomResponses = queryFactory
                .select(Projections.fields(ChatRoomResponse.class,
                        chatRoom.id.as("chatRoomId"),
                        chatRoom.product.thumbnailUrl,
                        chatRoom.lastSendTime,
                        chatRoom.subject.as("lastSendMessage"),
                        loginIdExpression,
                        profileExpression))
                .from(chatRoom)
                .where(equalsMemberId(memberId),
                        equalsItemId(itemId))
                .orderBy(chatRoom.lastSendTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable.getPageSize(), chatRoomResponses);
    }

    private Expression<String> createPartnerNameExpression(Long memberId) {
        return new CaseBuilder()
                .when(chatRoom.customer.id.eq(memberId))
                .then(chatRoom.seller.loginName)
                .otherwise(chatRoom.customer.loginName)
                .as("chatPartnerName");
    }

    private Expression<String> createPartnerProfileExpression(Long memberId) {
        return new CaseBuilder()
                .when(chatRoom.customer.id.eq(memberId))
                .then(chatRoom.seller.imgUrl)
                .otherwise(chatRoom.customer.imgUrl)
                .as("chatPartnerProfile");
    }

    private BooleanExpression equalsMemberId(Long memberId) {
        return chatRoom.customer.id.eq(memberId)
                .or(chatRoom.seller.id.eq(memberId));
    }

    private BooleanExpression equalsItemId(Long itemId) {
        if (itemId == null) {
            return null;
        }

        return chatRoom.product.id.eq(itemId);
    }
}
