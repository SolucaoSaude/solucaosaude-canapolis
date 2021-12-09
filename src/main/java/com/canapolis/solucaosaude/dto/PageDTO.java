package com.canapolis.solucaosaude.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO<T> {

    private Integer totalElements;
    private Integer totalPages;
    private boolean isFirst;
    private boolean isLast;
    private List<T> content;
}
