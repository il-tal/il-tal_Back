package com.example.sherlockescape.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequestDto {

	private String title;
	private String noticeContent;

}
