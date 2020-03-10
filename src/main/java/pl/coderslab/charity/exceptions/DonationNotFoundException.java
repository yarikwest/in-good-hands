package pl.coderslab.charity.exceptions;

public class DonationNotFoundException extends RuntimeException {
    public DonationNotFoundException() {
    }

    public DonationNotFoundException(long id) {
        super("Donation with id " + id + " not exists");
    }
}
