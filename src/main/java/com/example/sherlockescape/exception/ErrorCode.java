package com.example.sherlockescape.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	/* BAD_REQUEST 400 error*/
	BAD_PASSWORD(HttpStatus.BAD_REQUEST.value(), "Password incorrect", "비밀번호를 확인하세요"),
	TENDENCY_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), "Tendency already exist", "성향을 이미 등록하셨습니다."),
	MEMBER_BADGE_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), "MemberBadge already exist", "이미 획득한 칭호 입니다."),
	SUCCESS_NOT_ENOUGH(HttpStatus.BAD_REQUEST.value(), "Success count not enough", "성공 횟수가 부족합니다."),
	FAIL_NOT_ENOUGH(HttpStatus.BAD_REQUEST.value(), "Fail count not enough", "실패 횟수가 부족합니다."),

	/*UNAUTHORIZED 401 error*/

	/*FORBIDDEN 403 error*/
	NEED_TO_LOGIN(HttpStatus.FORBIDDEN.value(), "You Need To LogIn", "로그인이 필요합니다."),
	AUTHOR_IS_DIFFERENT(HttpStatus.FORBIDDEN.value(), "You are not a writer", "회원님이 작성한 글이 아닙니다."),

	/*Not Found 404 error*/
	COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Company not Found", "업체가 존재 하지 않습니다."),
	THEME_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Theme not Found", "테마가 존재 하지 않습니다."),
	BADGE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Badge not Found", "뱃지가 존재 하지 않습니다."),
	TENDENCY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Tendency not Found", "성향이 존재 하지 않습니다."),
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Review not Found", "리뷰가 존재 하지 않습니다."),
	NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Notice not Found", "공지가 존재 하지 않습니다."),


	/* CONFLICT 409 error*/
	BAD_PASSWORD_CONFIRM(HttpStatus.CONFLICT.value(), "Password and PasswordConfirm don't match", "비밀번호와 비밀번호 확인이 다릅니다."),
	DUPLICATE_MEMBER_ID(HttpStatus.CONFLICT.value(), "Member is duplicated", "중복된 사용자 ID가 존재합니다."),
	DUPLICATE_MEMBER_NICKNAME(HttpStatus.CONFLICT.value(), "Nickname is duplicated", "중복된 닉네임 ID가 존재합니다."),
	/*500 server error*/
	MEMBER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Member not found", "멤버를 찾을 수 없습니다.");

	private final Integer httpStatus;
	private final String message;
	private final String detail;
}