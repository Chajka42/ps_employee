package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.unisafe.psemployee.model.Tts;

public interface TtsRepository extends R2dbcRepository<Tts, Integer> {
}
