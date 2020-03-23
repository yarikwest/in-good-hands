package pl.coderslab.charity.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "donations")
public class Donation extends BaseEntity {

    @Min(value = 1)
    Integer quantity;

    @NotBlank
    String street;

    @NotBlank
    String city;

    @NotBlank
    String zipCode;

    @NotBlank
    String phone;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate pickUpDate;

    @NotNull
    LocalTime pickUpTime;

    @NotBlank
    String pickUpComment;

    @NotNull
    @Enumerated(EnumType.STRING)
    DonationStatus status = DonationStatus.MISSED;

    @NotNull(message = "{institutionNotSelect.error.message}")
    @ManyToOne
    Institution institution;

    @ManyToOne
    User user;
    @Size(min = 1, message = "{categoryNotSelect.error.message}")
    @ManyToMany
    @JoinTable(name = "donations_categories",
            joinColumns = @JoinColumn(name = "donation_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories = new HashSet<>();
}
