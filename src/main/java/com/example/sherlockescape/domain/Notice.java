package com.example.sherlockescape.domain;

import com.example.sherlockescape.domain.base.BaseTimeEntity;
import com.example.sherlockescape.dto.request.NoticeRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String title;

	@Column
	private String notice;

	@Column
	private String noticeImgUrl;

	public void update(NoticeRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.notice = requestDto.getNotice();
		this.noticeImgUrl = requestDto.getNoticeImgUrl();
	}
}
