package pl.coderslab.charity.exceptions;

import javax.persistence.EntityNotFoundException;

public class RoleNotFoundException extends EntityNotFoundException {
    public RoleNotFoundException() {
        super("Role not found");
    }
}
