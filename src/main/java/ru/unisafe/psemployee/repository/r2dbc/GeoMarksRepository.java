package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import ru.unisafe.psemployee.model.GeoMark;

@Repository
public interface GeoMarksRepository extends R2dbcRepository<GeoMark, Integer> {

}
