package com.hello.capston.dto.dto.uitls;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class PagingDto {

    private long totalPage;
    private Map<Long, Long> map = new LinkedHashMap<>();
}
