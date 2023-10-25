//package com.secondhand.presentation.controller;
//
//import com.secondhand.domain.chat.ChatRoom;
//import com.secondhand.domain.chat.CustomSlice;
//import com.secondhand.domain.chat.dto.ChatRoomResponse;
//import com.secondhand.domain.chat.dto.ChatroomDeatail;
//import com.secondhand.domain.chat.dto.request.ChatRequest;
//import com.secondhand.domain.chat.repository.ChatRoomRedisRepository;
//import com.secondhand.domain.chat.service.ChatRoomFacadeService;
//import com.secondhand.presentation.support.LoginCheck;
//import com.secondhand.presentation.support.LoginValue;
//import com.secondhand.util.BasicResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@RequiredArgsConstructor
//@Controller
//@RequestMapping("/api/chats")
//public class ChatRoomController {
//    private final ChatRoomRedisRepository chatRoomRedisRepository;
//    private final ChatRoomFacadeService chatRoomFacadeService;
//
//    @Operation(
//            summary = "채팅방 리스트 조회",
//            tags = "Chatroom",
//            description = "사용자는 채팅방 리스트를 조회할 수 있다."
//    )
//    @LoginCheck
//    @GetMapping()
//    public BasicResponse<CustomSlice<ChatRoomResponse>> room(@PageableDefault Pageable pageable,
//                                                             @LoginValue long userId) {
//        CustomSlice<ChatRoomResponse> chatRooms = chatRoomFacadeService.read(userId, pageable, null);
//
//        // chatRoomRequests.put(deferredResult, memberId);
//        return BasicResponse.send(HttpStatus.OK.value(),"채팅방 리스트가 조회되었습니다.", chatRooms);
//    }
//
//    @Operation(
//            summary = "채팅방 정보 단일 조회",
//            tags = "Chatroom",
//            description = "사용자는 채팅방 정보를 조회할 수 있다."
//    )
//    @GetMapping("/rooms/enter/{roomId}")
//    public BasicResponse<ChatroomDeatail> roomDetail(Model model, @PathVariable String roomId) {
//        ChatroomDeatail roomDetail = chatRoomFacadeService.findRoomDetail();
//        return BasicResponse.send(HttpStatus.OK.value(),"채팅방 단일 정보가 조회되었습니다..", roomDetail);
//    }
//
//    //채팅은 아이템에 대해 채팅을 한다.
//    @Operation(
//            summary = "채팅방 생성",
//            tags = "Chatroom",
//            description = "사용자는 채팅방을 생성할 수 있다."
//    )
//    @LoginCheck
//    @PostMapping("/rooms")
//    public BasicResponse<Long> createRoom(@RequestBody ChatRequest chatRequest, @LoginValue long userId) {
//        long chatRoomId = chatRoomFacadeService.creatChatRoom(chatRequest.getProductId(), userId);
//        return BasicResponse.send(HttpStatus.OK.value(),"채팅방이 생성 되었습니다..", chatRoomId);
//    }
//
//
//    @Operation(
//            summary = "채팅방 ID값 반환",
//            tags = "Chatroom",
//            description = "채티방 ID값."
//    )
//    @GetMapping("/room/{roomId}")
//    @ResponseBody
//    public ChatRoom roomInfo(@PathVariable String roomId) {
//        return chatRoomRedisRepository.findRoomById(roomId);
//    }
//
////    @Operation(
////            summary = "채팅방 완전히 나가기",
////            tags = "Chatroom",
////            description = "사용자는 채팅방을 완전히 나갈 수 있다."
////    )
////    @DeleteMapping("/{chatId}")
////    public GenericResponse<String> exitChatroom(@PathVariable String chatId, @RequestAttribute MemberDetails loginMember) throws ExistMemberIdException, ExistChatRoomException, NotChatroomMemberException {
////        chatRoomFacade.exitChatroom(chatId, loginMember.getId());
////
////        return GenericResponse.send("채탕방을 나갔습니다.", null);
////    }
//}
