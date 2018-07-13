package ua.slavik.carwash.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerDTO {
    private List<Long> carIds;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}
