package pl.coderslab.charity.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {
    String name;

    @Override
    public String getAuthority() {
        return getName();
    }
}
