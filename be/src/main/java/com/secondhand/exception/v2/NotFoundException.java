package com.secondhand.exception.v2;

public class NotFoundException extends SecondHandException {

    private static final String ITEM_NOT_FOUND_MESSAGE_FORMAT = "%s 번호의 아이템을 찾을 수 없습니다.";
    private static final String REGION_NOT_FOUND_MESSAGE_FORMAT = "아이디 %s를 가진 지역을 찾을 수 없습니다.";

    public NotFoundException(ErrorMessage errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorMessage errorCode, String message) {
        super(errorCode, message);
    }

    public static NotFoundException productNotFound(ErrorMessage errorCode, Long productId) {
        return new NotFoundException(errorCode, String.format(ITEM_NOT_FOUND_MESSAGE_FORMAT, productId));
    }

    public static NotFoundException townNotFound(ErrorMessage errorCode, Long townsId) {
        return new NotFoundException(errorCode, String.format(REGION_NOT_FOUND_MESSAGE_FORMAT, townsId));
    }
}
