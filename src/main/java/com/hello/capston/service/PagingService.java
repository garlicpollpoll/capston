package com.hello.capston.service;

import com.hello.capston.dto.dto.PagingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PagingService {

    /**
     * 페이징 추상화
     * @param visiblePage       몇개 보여주고 싶은지 PageRequest.of(0, 이부분);
     * @param pageNow           컨트롤러에서 받아온 현재 페이지
     * @param listSize          totalPage 를 구하기 위한 List<>의 사이즈
     * @return
     */
    public PagingDto paging(int visiblePage, int pageNow, long listSize) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        pageNow += 1;

        long pageStart, pageEnd;

        long size = listSize;

        long totalPage = 0;

        if (size % visiblePage == 0) {
            totalPage = size / visiblePage;
        }
        else {
            totalPage = size / visiblePage + 1;
        }

        pageStart = pageNow - 2;
        pageEnd = pageNow + 2;

        if (pageStart == 0 || pageStart == -1) {
            pageStart = 1;
            if (totalPage < 5) {
                pageEnd = totalPage;
            }
            else {
                pageEnd = 5;
            }
        } else if (pageEnd == totalPage + 1) { // 마지막 하나 전 페이지
            pageEnd = totalPage;
            if (totalPage < 5) {
                pageStart = 1;
            }
            else {
                pageStart = pageNow - 3;
            }
        } else if (pageEnd == totalPage + 2) { // 마지막 페이지
            pageEnd = totalPage;
            if (totalPage < 5) {
                pageStart = 1;
            }
            else {
                pageStart = pageNow - 4;
            }
        }

        Map<Long, Long> map = new LinkedHashMap<>();

        for (long i = pageStart; i <= pageEnd; i++) {
            map.put(i, i);
        }

        PagingDto dto = new PagingDto();

        dto.setTotalPage(totalPage);
        dto.setMap(map);

        return dto;
    }
}
