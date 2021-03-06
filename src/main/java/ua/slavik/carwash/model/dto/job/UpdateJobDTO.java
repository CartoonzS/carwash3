package ua.slavik.carwash.model.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.slavik.carwash.model.enums.Status;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;
}
