package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.unisafe.psemployee.model.Partner;

public interface PartnerRepository extends R2dbcRepository<Partner, Integer> {
}
