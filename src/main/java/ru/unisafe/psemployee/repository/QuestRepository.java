package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class QuestRepository {
    private final DatabaseClient databaseClient;

    public Mono<Void> updateQuestProgress(int visorId, boolean isUpdate, boolean isNoCuts, boolean isDefect, boolean isPlan, boolean isFrod) {
        return databaseClient.sql("""
                UPDATE quests 
                SET total_visits = total_visits + 1, 
                    updates = updates + :isUpdate, 
                    no_cuts = no_cuts + :isNoCuts, 
                    no_plan = no_plan + :isPlan, 
                    frod = frod + :isFrod, 
                    defect = defect + :isDefect
                WHERE visor_id = :visorId 
                ORDER BY date DESC 
                LIMIT 1
                """)
                .bind("isUpdate", isUpdate ? 1 : 0)
                .bind("isNoCuts", isNoCuts ? 1 : 0)
                .bind("isPlan", isPlan ? 1 : 0)
                .bind("isFrod", isFrod ? 1 : 0)
                .bind("isDefect", isDefect ? 1 : 0)
                .bind("visorId", visorId)
                .fetch()
                .rowsUpdated()
                .then();
    }
}
