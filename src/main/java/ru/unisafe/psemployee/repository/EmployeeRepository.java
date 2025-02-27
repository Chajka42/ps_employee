package ru.unisafe.psemployee.repository;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.Employee;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class EmployeeRepository {

    private final DSLContext dsl;

    public EmployeeRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Flux<Employee> findAll() {
        return Flux.from(dsl
                        .select()
                        .from("employee"))
                .map(this::mapToEmployee);
    }

    public Mono<Employee> findById(int id) {
        return Mono.from(dsl
                        .select()
                        .from("employee")
                        .where(field("id").eq(id)))
                .map(this::mapToEmployee);
    }

    public Mono<Void> save(Employee employee) {
        return Mono.from(dsl
                        .insertInto(table("employee"),
                                field("token"), field("name"), field("access"), field("partner_id"))
                        .values(employee.token(), employee.name(), employee.access(), employee.partnerId()))
                .then();
    }

    private Employee mapToEmployee(Record record) {
        return new Employee(
                record.get(field("id", Integer.class)),
                record.get(field("token", String.class)),
                record.get(field("name", String.class)),
                record.get(field("access", Integer.class)),
                record.get(field("partner_id", Integer.class))
        );
    }
}
