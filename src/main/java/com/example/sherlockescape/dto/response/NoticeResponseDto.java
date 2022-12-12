package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeResponseDto {

	private Long id;
	private String title;
	private String noticeContent;
	private String noticeImgUrl;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

}
