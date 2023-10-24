package com.secondhand.presentation.controller;

import com.secondhand.presentation.support.LoginCheck;
import com.secondhand.presentation.support.LoginValue;
import com.secondhand.service.TownService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.requset.TownRegisterRequest;
import com.secondhand.web.dto.requset.TownRequest;
import com.secondhand.web.dto.response.TownResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(tags = "지역")
@RestController
@RequestMapping("/api/towns")
@RequiredArgsConstructor
public class TownController {

    private final TownService townService;

    @Operation(
            summary = "전체 동네 목록을 조회", description = "사용자는 전체 동네 목록을 볼수 있다.."
    )
    @LoginCheck
    @GetMapping
    public BasicResponse<List<TownResponse>> read() {
        List<TownResponse> townList = townService.findByAll();
        log.debug("전체 동네 목록을 가져온다 = {}", townList);

        return BasicResponse.send(HttpStatus.OK.value(), "성공", townList);
    }

    @Operation(
            summary = "사용자가 등록한 동네.", description = "사용자는 특정 동네를 가져올 수있다."
    )
    @LoginCheck
    @GetMapping("/member")
    public BasicResponse<List<TownResponse>> readRegisterByMember(@LoginValue long userId) {
        List<TownResponse> townDetail = townService.findTownDetail(userId);
        log.debug("사용자가 등록한 동네를 가져올수 있다  = {}", townDetail);

        return BasicResponse.send(HttpStatus.OK.value(),"사용자가 등록한 동네를 가져올수 있다", townDetail);
    }

    //TODO : 등록을 숫자로가지고한다? 수정필요
    @Operation(
            summary = "사용자의 처음 동네 등록", description = "사용자의 화원가입할 때 메인, 서브  동네를 등록할 수있다."
    )
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

    @Operation(
            summary = "사용자의 동네 수정", description = "사용자의 메인, 서브 동네를 수정할 수 있다."
    )
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
