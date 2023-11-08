package com.secondhand.presentation.controller;

import com.secondhand.domain.chat.CustomSlice;
import com.secondhand.domain.chat.dto.ChatRoomResponse;
import com.secondhand.domain.chat.dto.request.ChatRequest;
import com.secondhand.presentation.support.LoginValue;
import com.secondhand.service.ChatLogService;
import com.secondhand.service.ChatRoomService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.chat.ChatLogResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ChatController {

    private static final Long TIMEOUT_VALUE = 10000L;

    private final Map<DeferredResult<BasicResponse<ChatLogResponse>>, ChatData> chatRequests = new ConcurrentHashMap<>();
    private final Map<DeferredResult<BasicResponse<CustomSlice<ChatRoomResponse>>>, Long> chatRoomRequests = new ConcurrentHashMap<>();
    private final ChatLogService chatLogService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/chats/{chatRoomId}")
    public DeferredResult<BasicResponse<ChatLogResponse>> readAll(
            @PathVariable Long chatRoomId,
            @RequestParam(required = false, defaultValue = "0") Long messageId,
            @LoginValue Long memberId) {

        DeferredResult<BasicResponse<ChatLogResponse>> deferredResult = new DeferredResult<>(
                TIMEOUT_VALUE, BasicResponse.send(HttpStatus.OK.value(), "채팅룸 보기", List.of()));
        chatRequests.put(deferredResult, new ChatData(chatRoomId, messageId, memberId));

        deferredResult.onCompletion(() -> chatRequests.remove(deferredResult));

        ChatLogResponse messages = chatLogService.getMessages(chatRoomId, messageId, memberId);
        if (!messages.getChat().isEmpty()) {
            deferredResult.setResult(
                    BasicResponse.send(HttpStatus.OK.value(), "채팅방이 없습니다", messages));
        }

        return deferredResult;
    }

    @GetMapping("/chats")
    public DeferredResult<BasicResponse<CustomSlice<ChatRoomResponse>>> readList(
            @PageableDefault Pageable pageable,
            @LoginValue Long memberId) {
        CustomSlice<ChatRoomResponse> chatRooms = chatRoomService.read(memberId, pageable, null);

        DeferredResult<BasicResponse<CustomSlice<ChatRoomResponse>>> deferredResult =
                new DeferredResult<>(TIMEOUT_VALUE,
                        BasicResponse.send(HttpStatus.OK.value(), "채팅목록 보기", chatRooms));
        chatRoomRequests.put(deferredResult, memberId);

        deferredResult.onCompletion(() -> chatRoomRequests.remove(deferredResult));

        return deferredResult;
    }

    @GetMapping("/items/{productId}/chats")
    public DeferredResult<BasicResponse<CustomSlice<ChatRoomResponse>>> readListByItem(
            @PageableDefault Pageable pageable,
            @PathVariable Long productId,
            @LoginValue Long memberId) {
        CustomSlice<ChatRoomResponse> chatRooms = chatRoomService.read(memberId, pageable,
                productId);

        DeferredResult<BasicResponse<CustomSlice<ChatRoomResponse>>> deferredResult =
                new DeferredResult<>(TIMEOUT_VALUE,
                        BasicResponse.send(HttpStatus.OK.value(), "채팅에 해당하는 상품 보기", chatRooms));
        chatRoomRequests.put(deferredResult, memberId);

        deferredResult.onCompletion(() -> chatRoomRequests.remove(deferredResult));

        return deferredResult;
    }

    @PostMapping("/chats/{chatRoomId}")
    public BasicResponse<Void> sendMessage(
            @Valid @RequestBody ChatRequest request,
            @PathVariable Long chatRoomId,
            @LoginValue Long senderId) {
        Long receiverId = chatRoomService.getReceiverId(chatRoomId);
        chatLogService.sendMessage(request.getMessage(), chatRoomId, senderId);

        for (var entry : chatRequests.entrySet()) {
            ChatData chatData = entry.getValue();

            if (chatData.getChatRoomId().equals(chatRoomId)) {
                ChatLogResponse messages = chatLogService.getMessages(chatRoomId,
                        chatData.getChatRoomId(),
                        chatData.getTargetMemberId());
                entry.getKey()
                        .setResult(BasicResponse.send(HttpStatus.OK.value(), "채팅 로그 보내기 성공",
                                messages));
            }
        }

        for (var entry : chatRoomRequests.entrySet()) {
            if (entry.getValue().equals(receiverId)) {
                CustomSlice<ChatRoomResponse> chatRooms = chatRoomService.read(senderId,
                        Pageable.ofSize(10), null);
                entry.getKey().setResult(
                        BasicResponse.send(HttpStatus.OK.value(), "채팅방 정보 보내기 성공", chatRooms));
            }
        }

        return BasicResponse.send(HttpStatus.OK.value(), "성공");
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/items/{itemId}/chats")
    public BasicResponse<Map<String, Long>> createChatRoom(@PathVariable Long itemId,
            @LoginValue Long senderId) {
        Long chatRoomId = chatRoomService.createChatRoom(itemId, senderId);
        return BasicResponse.send(HttpStatus.CREATED.value(), "채팅방 만들기",
                Map.of("chatRoomId", chatRoomId));
    }


    @Getter
    @AllArgsConstructor
    private static class ChatData {

        private Long chatRoomId;
        private Long messageId;
        private Long targetMemberId;
    }
}
