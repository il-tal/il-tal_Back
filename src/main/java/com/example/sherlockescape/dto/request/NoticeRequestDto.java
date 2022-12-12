package com.example.sherlockescape.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
public class NoticeRequestDto {

	private String title;
	private String notice;
	private String noticeImgUrl;

}
