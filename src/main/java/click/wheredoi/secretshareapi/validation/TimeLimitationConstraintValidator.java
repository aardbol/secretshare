/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class TimeLimitationConstraintValidator implements ConstraintValidator<TimeLimitation, Timestamp> {
    private long min;
    private long max;

    @Override
    public void initialize(TimeLimitation timeLimitation) {
        max = TimeUnit.MINUTES.toMillis(timeLimitation.max());
        min = TimeUnit.MINUTES.toMillis(timeLimitation.min());
    }

    @Override
    public boolean isValid(Timestamp value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long timeDifference = Math.abs(value.getTime() - currentTime.getTime());

        return (min == 0 || timeDifference >= min) && (max == 0 || timeDifference <= max);
    }
}
