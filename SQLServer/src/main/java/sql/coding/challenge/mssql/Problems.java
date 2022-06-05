package sql.coding.challenge.mssql;

import static mssql.jooq.generated.Tables.EMPLOYEE;
import static mssql.jooq.generated.tables.Sale.SALE;
import org.jooq.CloseableDSLContext;
import org.jooq.impl.DSL;

public final class Problems {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=classicmodels";
    private static final String USERNAME = "sa";
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
