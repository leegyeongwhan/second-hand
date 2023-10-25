package com.secondhand.presentation.controller;

import com.secondhand.presentation.support.LoginCheck;
import com.secondhand.presentation.support.LoginValue;
import com.secondhand.service.TownService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.requset.TownRegisterRequest;
import com.secondhand.web.dto.requset.TownRequest;
import com.secondhand.web.dto.response.TownResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/towns")
@RequiredArgsConstructor
public class TownController {

    private final TownService townService;

    @LoginCheck
    @GetMapping
    public BasicResponse<List<TownResponse>> read() {
        List<TownResponse> townList = townService.findByAll();
        log.debug("전체 동네 목록을 가져온다 = {}", townList);

        return BasicResponse.send(HttpStatus.OK.value(), "성공", townList);
    }

    @LoginCheck
    @GetMapping("/member")
    public BasicResponse<List<TownResponse>> readRegisterByMember(@LoginValue long userId) {
        List<TownResponse> townDetail = townService.findTownDetail(userId);
        log.debug("사용자가 등록한 동네를 가져올수 있다  = {}", townDetail);

        return BasicResponse.send(HttpStatus.OK.value(),"사용자가 등록한 동네를 가져올수 있다", townDetail);
    }

    @LoginCheck
    @PostMapping
    public BasicResponse<String> registerTown(@LoginValue long userId,
                                              @RequestBody TownRegisterRequest request) {

        if (request.getTownId() == null) {
            throw new IllegalArgumentException("필수 지역 정보 없음");
        }

        townService.save(userId, request.getTownId());

        return BasicResponse.send(HttpStatus.OK.value(),"사용자의 처음 동네 등록");
    }

    @LoginCheck
    @PatchMapping
    public BasicResponse<String> updateTown(@LoginValue long userId,
                                            final @Valid @RequestBody TownRequest townRequest) {

        if (townRequest.getTownsId()[0] == null) {
            throw new IllegalArgumentException("필수 지역 정보 없음");
        }

        townService.update(userId, townRequest);

        return BasicResponse.send(HttpStatus.OK.value(),"사용자의 동네 수정");
    }
}
