package com.nameslowly.coinauctions.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResultCase {

    /* 성공 0번대 - 모든 성공 응답을 200으로 통일 */
    SUCCESS(HttpStatus.OK, 0, "정상 처리 되었습니다."),

    // 권한 없음 403
    NOT_AUTHORIZED(HttpStatus.FORBIDDEN, 1000, "해당 요청에 대한 권한이 없습니다."),
    // 잘못된 형식의 입력 400
    INVALID_INPUT(HttpStatus.BAD_REQUEST, 1001, "유효하지 않은 입력값입니다."),
    // 존재하지 않는 값 404
    NOT_FOUND(HttpStatus.NOT_FOUND, 1002, "존재하지 않는 입력값입니다."),
    // 시스템 에러 500
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1003, "알 수 없는 에러가 발생했습니다."),

    /* 유저 2000번대 */

    // 존재하지 않는 사용자 404,
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 2000, "유저를 찾을 수 없습니다."),
    // 로그인 필요 401
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, 2001, "로그인이 필요합니다."),
    // 중복된 유저네임 입력 409
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, 2002, "중복된 유저네임을 입력하셨습니다."),
    // 유효하지 않은 토큰 401
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 2003, "유효하지 않은 토큰입니다."),
    // 만료된 액세스 토큰 401
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 2004, "만료된 Access Token"),
    // 만료된 리프레쉬 토큰 401
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 2005, "만료된 Refresh Token"),

    /* 서비스별로 분기 */

    /* auction-server 4000 */
    NOT_FOUND_AUCTION(HttpStatus.BAD_REQUEST, 4001, "해당 경매 찾을 수 없음"),
    NOT_PENDING_AUCTION(HttpStatus.BAD_REQUEST, 4002, "대기 중인 경매 아님"),
    NOT_ONGOING_AUCTION(HttpStatus.BAD_REQUEST, 4003, "진행 중인 경매 아님"),
    NOT_END_AUCTION(HttpStatus.BAD_REQUEST, 4003, "끝나지 않은 경매"),

    /* bidwin-server 5000 */
    NOT_ENOUGH_USER_COIN_AMOUNT(HttpStatus.BAD_REQUEST, 5002, "유저 코인 부족"),
    NOT_ENOUGH_THAN_CURRENT_PRICE(HttpStatus.BAD_REQUEST, 5003, "현재 입찰 보다 적음"),
    NOT_ENOUGH_THAN_BASE_AMOUNT(HttpStatus.BAD_REQUEST, 5004, "경매 시작가 보다 적음"),
    NEW_BID_CREATE_ERROR(HttpStatus.BAD_REQUEST, 5005, "새 입찰 생성 에러"),

    
    /* chat-server 6000 */
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, 6000, "채팅방이 존재하지 않습니다."),
    CHATROOM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 6001, "채팅방-멤버 정보가 존재하지 않습니다."),
    CHATMESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, 6002, "채팅 메시지가 존재하지 않습니다."),
    

    /* coinpay-server 7000 */
    COIN_NOT_FOUND(HttpStatus.NOT_FOUND, 7000, "코인을 찾을 수 없습니다."),
    COIN_WALLET_NOT_FOUND(HttpStatus.NOT_FOUND, 7001, "해당 코인과 유저에 해당하는 코인지갑을 찾을 수 없습니다."),
    INVALID_COIN_PRICE(HttpStatus.BAD_REQUEST, 7002, "해당 코인의 값이 유효하지 않습니다."),
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, 7003, "해당 충전금액이 유효하지 않습니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, 7004, "코인 수량이 유효하지 않습니다."),
    NO_COINS_FOUND(HttpStatus.NOT_FOUND, 7005, "코인이 존재하지 않습니다."),
    COIN_PRICE_UPDATE_FAILED(HttpStatus.BAD_REQUEST, 7006, "코인 가격 업데이트에 실패했습니다"),
    UPBIT_API_ERROR(HttpStatus.BAD_REQUEST, 7007, "업비트 API를 불러오는데 실패했습니다."),
    COIN_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, 7008, "해당 코인은 이미 존재합니다."),
    NO_HISTORIES_FOUND(HttpStatus.NOT_FOUND, 7009, "코인 히스토리가 존재하지 않습니다."),
    /* 경로 8000번대 */
    ROUTE_NOT_INVALID(HttpStatus.BAD_REQUEST, 8000, "그런 경로 없습니다.");


    private final HttpStatus httpStatus; // 응답 상태 코드
    private final Integer code; // 응답 코드. 도메인에 따라 1000번대로 나뉨
    private final String message; // 응답에 대한 설명
}
