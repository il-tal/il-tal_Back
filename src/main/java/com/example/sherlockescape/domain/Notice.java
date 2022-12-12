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
	private String noticeContent;

	@Column
	private String noticeImgUrl;

	public void update(NoticeRequestDto requestDto, String imgUrl) {
		this.title = requestDto.getTitle();
		this.noticeContent = requestDto.getNoticeContent();
		this.noticeImgUrl = imgUrl;
	}
}