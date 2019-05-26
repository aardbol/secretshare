/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = TimeLimitationConstraintValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface TimeLimitation {
    String message() default "must be between set minimum and maximum values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return long Minimum time in minutes
     */
    long min() default 0;

    /**
     * @return long Maximum time in minutes
     */
    long max() default 0;
}
