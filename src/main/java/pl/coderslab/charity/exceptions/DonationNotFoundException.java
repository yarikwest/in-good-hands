package pl.coderslab.charity.exceptions;

import javax.persistence.EntityNotFoundException;

public class DonationNotFoundException extends EntityNotFoundException {

    public DonationNotFoundException(long id) {
        super("Donation with id " + id + " not exists");
    }
}
