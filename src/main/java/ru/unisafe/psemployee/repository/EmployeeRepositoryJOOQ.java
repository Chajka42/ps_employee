package ru.unisafe.psemployee.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.EmployeeDto;

import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class EmployeeRepositoryJOOQ {

    private final DSLContext dsl;

    public EmployeeRepositoryJOOQ(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Flux<EmployeeDto> findAll() {
        return Flux.from(dsl
                        .select()
                        .from("employee"))
                .map(this::mapToEmployee);
    }

    public Mono<EmployeeDto> findById(int id) {
        return Mono.from(dsl
                        .select()
                        .from("employee")
                        .where(field("id").eq(id)))
                .map(this::mapToEmployee);
    }

    public Mono<Void> save(EmployeeDto employee) {
        return Mono.from(dsl
                        .insertInto(table("employee"),
                                field("token"), field("name"), field("access"), field("partner_id"))
                        .values(employee.token(), employee.name(), employee.access(), employee.partnerId()))
                .then();
    }

    private EmployeeDto mapToEmployee(Record record) {
        return new EmployeeDto(
                record.get(field("id", Integer.class)),
                record.get(field("token", String.class)),
                record.get(field("name", String.class)),
                record.get(field("access", Integer.class)),
                record.get(field("partner_id", Integer.class))
        );
    }

    public Mono<Optional<EmployeeDto>> findEmployeeByPassFunction(String passFunction) {
        return Mono.from(dsl
                        .select(field("id", Integer.class),
                                field("name", String.class),
                                field("access", Integer.class),
                                field("token", String.class),
                                field("partner_id", Integer.class))
                        .from(table("employee"))
                        .where(field("pass_function").eq(passFunction))
                ).map(record -> Optional.of(mapToEmployee(record)))
                .defaultIfEmpty(Optional.empty());
    }

    public Mono<Boolean> checkAuthEmployee(String token) {
        return Mono.from(dsl
                        .selectOne()
                        .from("employee")
                        .where(field("token").eq(token)))
                .hasElement();
    }

    public Mono<Boolean> checkRelictAuthEmployee(String token) {
        return Mono.from(dsl
                        .selectOne()
                        .from("employee")
                        .where(field("relict_token").eq(token)))
                .hasElement();
    }

    public Mono<Integer> getEmployeeOriginId(String token) {
        return Mono.from(dsl
                        .select(field("id"))
                        .from("employee")
                        .where(field("token").eq(token).or(field("relict_token").eq(token))))
                .map(record -> record.get(field("id", Integer.class)));
    }

    public Mono<Void> updateEmployeeLocation(int id, double lat, double lon, String address) {
        return Mono.from(dsl
                        .update(table("employee"))
                        .set(field("lat"), lat)
                        .set(field("lon"), lon)
                        .set(field("address"), address)
                        .where(field("id").eq(id)))
                .then();
    }

    public Mono<String> getEmployeeName(String token) {
        return Mono.fromCallable(() -> dsl
                .select(field("name", String.class))
                .from("employee")
                .where(field("token", String.class).eq(token))
                .fetchOneInto(String.class)
        );
    }

    public Mono<String> getTokenByRelictToken(String relictToken) {
        Field<String> token = field("token", String.class);

        return Mono.from(dsl
                        .select(token)
                        .from(table("employee"))
                        .where(field("relict_token").eq(relictToken)))
                .map(record -> record.get(token));
    }
}
