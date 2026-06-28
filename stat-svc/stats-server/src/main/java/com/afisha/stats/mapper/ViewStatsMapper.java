package com.afisha.stats.mapper;

import com.afisha.stats.ViewStatsDto;
import com.afisha.stats.model.ViewStats;

public class ViewStatsMapper {

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }
}