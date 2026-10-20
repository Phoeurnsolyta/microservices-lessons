package kh.edu.istad.platform.customer.restapi.dto;

import java.util.UUID;

public record AssignCustomerSegmentRequest(
        UUID segmentId
) {
}
