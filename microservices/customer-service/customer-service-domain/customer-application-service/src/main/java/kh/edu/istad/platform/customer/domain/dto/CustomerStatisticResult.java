package kh.edu.istad.platform.customer.domain.dto;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatusStatistic;

import java.util.List;

public record CustomerStatisticResult (
        long totalCustomers,
        List<CustomerStatusStatistic> statistic
){
}
