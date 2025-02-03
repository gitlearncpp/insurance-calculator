package org.insurance.repository;

import org.insurance.model.InsuranceOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceOfferRepository extends JpaRepository<InsuranceOffer, Long> {
}
