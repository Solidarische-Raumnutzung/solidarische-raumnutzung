package edu.kit.hci.soli.config;

import com.google.auto.service.AutoService;
import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * This class registers custom SQL functions with Hibernate.
 */
@AutoService(FunctionContributor.class)
public class SQLFunctionContributor implements FunctionContributor {
    /**
     * Registers custom SQL functions with Hibernate.
     *
     * @param functionContributions the function contributions to which the custom functions are added
     */
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry()
                .register("DAY_OF_WEEK", new StandardSQLFunction("DAY_OF_WEEK", StandardBasicTypes.INTEGER));
    }
}