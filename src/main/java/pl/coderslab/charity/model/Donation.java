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
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "donations")
public class Donation extends BaseEntity {
    @Min(1)
    Integer quantity;
    @NotBlank
    String street;
    @NotBlank
    String city;
    @NotBlank
    String zipCode;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate pickUpDate;
    @NotNull
    LocalTime pickUpTime;
    @NotBlank
    String pickUpComment;
    @NotNull
    @ManyToOne
    Institution institution;
    @Size(min = 1)
    @ManyToMany
    @JoinTable(name = "donations_categories",
            joinColumns = @JoinColumn(name = "donation_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories = new HashSet<>();
}
