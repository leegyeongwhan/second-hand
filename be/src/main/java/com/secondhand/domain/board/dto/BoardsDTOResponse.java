package com.secondhand.domain.board.dto;


import com.secondhand.domain.board.Status;
import lombok.*;

@Getter
@Builder
public class BoardsDTOResponse {

    private String title;
    private String town;
    private String createdAt;
    private String price;
    private Status status;
    private CountInfo countInfo;
    private String img;
}
