package sql.coding.challenge.oracle;

import static oracle.jooq.generated.tables.Employee.EMPLOYEE;
import static oracle.jooq.generated.tables.EmployeeStatus.EMPLOYEE_STATUS;
import static oracle.jooq.generated.tables.Sale.SALE;
import org.jooq.CloseableDSLContext;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.max;

public final class Problems {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USERNAME = "CLASSICMODELS";
    private static final String PASSWORD = "classroot";
    
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
    
    /*
    Fetch the fiscal year and the maximum profit among all the sales of that 
    fiscal year of sales representatives having status AVERAGE.

    Note:
    1. profit = sale-(employee_salary + employee_commission)
    2. Order the result set by fiscal year
     */
    public static void sql002() {

        try ( CloseableDSLContext ctx = DSL.using(URL, USERNAME, PASSWORD)) {
            ctx.select(SALE.FISCAL_YEAR,
                    max(SALE.SALE_.minus(EMPLOYEE.SALARY.plus(EMPLOYEE.COMMISSION))).as("profit"))
                    .from(EMPLOYEE)
                    .innerJoin(SALE)
                    .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)
                            .and(EMPLOYEE.JOB_TITLE.like("Sale%")))
                    .innerJoin(EMPLOYEE_STATUS)
                    .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(EMPLOYEE_STATUS.EMPLOYEE_NUMBER))
                    .where(EMPLOYEE_STATUS.STATUS.eq("AVERAGE"))
                    .groupBy(SALE.FISCAL_YEAR)
                    .having(max(SALE.SALE_.minus(EMPLOYEE.SALARY.plus(EMPLOYEE.COMMISSION))).gt(0.0))
                    .orderBy(1)
                    .fetch();
        }
    }

    /*
    Write a SQL query to get the second highest salary from the Employee table.
     */
    public static void sql003() {

        try ( CloseableDSLContext ctx = DSL.using(URL, USERNAME, PASSWORD)) {
            ctx.selectDistinct(EMPLOYEE.SALARY)
                    .from(EMPLOYEE)
                    .orderBy(EMPLOYEE.SALARY.desc())
                    .limit(1)
                    .offset(1)
                    .fetch();
        }
    }
}

