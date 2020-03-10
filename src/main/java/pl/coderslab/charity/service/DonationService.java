package pl.coderslab.charity.service;

import pl.coderslab.charity.model.Donation;

import java.util.Set;

public interface DonationService {
    Set<Donation> getAll();

    Donation getById(long id);

    Donation create(Donation donation);

}
