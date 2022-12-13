package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Notice;
import com.example.sherlockescape.dto.response.NoticeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeRepositoryCustom {

	NoticeResponseDto getDetailNotice(Long noticeId);
	Page<NoticeResponseDto> getAllNotices(Pageable pageable);


}
