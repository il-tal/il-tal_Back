package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

	Page<Notice> getNoticeList(Pageable pageable);

}
