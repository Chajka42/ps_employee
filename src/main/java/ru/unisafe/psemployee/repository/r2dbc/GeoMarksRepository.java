package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.unisafe.psemployee.model.GeoMark;

public interface GeoMarksRepository extends R2dbcRepository<GeoMark, Integer> {

}
