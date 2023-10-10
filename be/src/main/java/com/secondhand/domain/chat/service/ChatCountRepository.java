package com.secondhand.domain.chat.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.secondhand.domain.chat.QChatLog.chatLog;
import static com.secondhand.domain.chat.QChatRoom.chatRoom;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatCountRepository {

    private final JPAQueryFactory queryFactory;

    public Map<Long, Long> countNewMessage(Long memberId) {
        List<Tuple> results = queryFactory
                .select(chatLog.chatRoom.id,
                        chatLog.chatRoom.id.count())
                .from(chatLog)
                .join(chatLog.chatRoom, chatRoom)
                .where(isUnread()
                        .and(equalsMemberId(memberId)))
                .groupBy(chatLog.chatRoom.id) //각 메시지는 특정 채팅 방에 속해있고, 사용자마다 읽지 않은 메시지를 확인해야한다.
                .fetch();

        log.debug("results = {} ", results);

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, Long.class),  // ChatRoomId
                        tuple -> Optional.ofNullable(tuple.get(1, Long.class)).orElse(0L)  // newMessageCount
                ));
    }

    private BooleanExpression isUnread() {
        return chatLog.readCount.eq(1);
    }

    private BooleanExpression equalsMemberId(Long memberId) {
        return chatLog.senderId.ne(memberId);
    }
}
