package com.itau.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.DoubleSummaryStatistics;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaResponse {

    private long count;
    private double sum;
    private double avg;
    private double min;
    private double max;

    public static EstatisticaResponse fromDoubleSummaryStatistics(DoubleSummaryStatistics stats) {
        if (stats.getCount() == 0) {
            return EstatisticaResponse.builder()
                    .count(0)
                    .sum(0.0)
                    .avg(0.0)
                    .min(0.0)
                    .max(0.0)
                    .build();
        }

        return EstatisticaResponse.builder()
                .count(stats.getCount())
                .sum(stats.getSum())
                .avg(stats.getAverage())
                .min(stats.getMin())
                .max(stats.getMax())
                .build();
    }
}
