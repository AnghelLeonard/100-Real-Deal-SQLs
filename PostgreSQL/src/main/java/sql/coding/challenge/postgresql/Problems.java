package sql.coding.challenge.postgresql;

import org.jooq.CloseableDSLContext;
import org.jooq.impl.DSL;
import static postgresql.jooq.generated.Tables.SALE;
import static postgresql.jooq.generated.tables.Employee.EMPLOYEE;

public final class Problems {

    private static final String URL = "jdbc:postgresql://localhost:5432/classicmodels";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";

    private Problems() {
        throw new AssertionError("Cannot instantiate");
    }

    /*
    Select the employee's last name and email having the job title 
    starting with 'Sale' and the sales after 2005 are greater than 1000.
     */
    public static void sql001() {

        try ( CloseableDSLContext ctx = DSL.using(URL, USERNAME, PASSWORD)) {
            ctx.select(EMPLOYEE.LAST_NAME, EMPLOYEE.EMAIL)
                    .from(EMPLOYEE)
                    .innerJoin(SALE)
                    .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                    .where(EMPLOYEE.JOB_TITLE.like("Sale%")
                            .and(SALE.FISCAL_YEAR.gt(2005))
                            .and(SALE.SALE_.gt(1000.0)))
                    .fetch();
        }
    }
}
