package com.secondhand.presentation.controller;

import com.secondhand.presentation.support.LoginValue;
import com.secondhand.service.ChatLogService;
import com.secondhand.service.ChatRoomService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.chat.ChatLogResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ChatController {

    private static final Long TIMEOUT_VALUE = 10000L;

    private final Map<DeferredResult<BasicResponse<ChatLogResponse>>, ChatData> chatRequests = new ConcurrentHashMap<>();
    private final ChatLogService chatLogService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/chats/{chatRoomId}")
    public DeferredResult<BasicResponse<ChatLogResponse>> readAll(
            @PathVariable Long chatRoomId,
            @RequestParam(required = false, defaultValue = "0") Long messageId,
            @LoginValue Long memberId) {

        DeferredResult<BasicResponse<ChatLogResponse>> deferredResult =
                new DeferredResult<>(TIMEOUT_VALUE, BasicResponse.send(HttpStatus.OK.value(), "채팅룸 보기", List.of()));
        chatRequests.put(deferredResult, new ChatData(chatRoomId, messageId, memberId));

        deferredResult.onCompletion(() -> chatRequests.remove(deferredResult));

        ChatLogResponse messages = chatLogService.getMessages(chatRoomId, messageId, memberId);
        if (!messages.getChat().isEmpty()) {
            deferredResult.setResult(BasicResponse.send(HttpStatus.OK.value(),"채팅방이 없습니다" ,messages));
        }

        return deferredResult;
    }

//    @GetMapping("/chats")
//    public DeferredResult<ApiResponse<CustomSlice<ChatRoomResponse>>> readList(
//            @PageableDefault Pageable pageable,
//            @Auth Long memberId) {
//        CustomSlice<ChatRoomResponse> chatRooms = chatRoomService.read(memberId, pageable, null);
//
//        DeferredResult<ApiResponse<CustomSlice<ChatRoomResponse>>> deferredResult =
//                new DeferredResult<>(TIMEOUT_VALUE, new ApiResponse<>(HttpStatus.OK.value(), chatRooms));
//        chatRoomRequests.put(deferredResult, memberId);
//
//        deferredResult.onCompletion(() -> chatRoomRequests.remove(deferredResult));
//
//        return deferredResult;
//    }


    @Getter
    @AllArgsConstructor
    private static class ChatData {

        private Long chatRoomId;
        private Long messageId;
        private Long targetMemberId;
    }
}
