/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SecretDTOTest {
    private Validator validator;
    private SecretDTO secretDTO;

    @BeforeEach
    void setUp() {
        secretDTO = new SecretDTO();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgConstructor() {
        assertThat(SecretDTO.class, hasValidBeanConstructor());
    }

    @Test
    void gettersAndSettersShouldWorkForEachProperty() {
        assertThat(SecretDTO.class, hasValidGettersAndSetters());
    }

    @Test
    void dataIsNullReturnsValidationError() {
        secretDTO.setExpires(TimeUnit.DAYS.toMinutes(30));
        Set<ConstraintViolation<SecretDTO>> violations = validator.validate(secretDTO);

        assertEquals(1, violations.size());
        assertEquals("{javax.validation.constraints.NotEmpty.message}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    void dataIsEmptyReturnsValidationError() {
        secretDTO.setExpires(TimeUnit.DAYS.toMinutes(30));
        secretDTO.setData("");
        Set<ConstraintViolation<SecretDTO>> violations = validator.validate(secretDTO);

        assertEquals(1, violations.size());
        assertEquals("{javax.validation.constraints.NotEmpty.message}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    void expiresIsNullReturnsValidationError() {
        secretDTO.setData("hpYKofvmUoE=");
        Set<ConstraintViolation<SecretDTO>> violations = validator.validate(secretDTO);

        assertEquals(1, violations.size());
        assertEquals("{javax.validation.constraints.Min.message}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    void expiresNotRespectingMinimumReturnsValidationError() {
        secretDTO.setData("hpYKofvmUoE=");
        secretDTO.setExpires(-5);
        Set<ConstraintViolation<SecretDTO>> violations = validator.validate(secretDTO);

        assertEquals(1, violations.size());
        assertEquals("{javax.validation.constraints.Min.message}", violations.iterator().next().getMessageTemplate());

        secretDTO.setExpires(4);
        Set<ConstraintViolation<SecretDTO>> violations2 = validator.validate(secretDTO);

        assertEquals(1, violations.size());
        assertEquals("{javax.validation.constraints.Min.message}", violations2.iterator().next().getMessageTemplate());
    }

    @Test
    void expiresNotRespectingMaximumReturnsValidationError() {
        secretDTO.setData("hpYKofvmUoE=");
        secretDTO.setExpires(TimeUnit.DAYS.toMinutes(31));
        Set<ConstraintViolation<SecretDTO>> violations = validator.validate(secretDTO);

        assertEquals(1, violations.size());
        assertEquals("{javax.validation.constraints.Max.message}", violations.iterator().next().getMessageTemplate());

        secretDTO.setExpires(TimeUnit.DAYS.toMinutes(1000));
        Set<ConstraintViolation<SecretDTO>> violations2 = validator.validate(secretDTO);

        assertEquals(1, violations.size());
        assertEquals("{javax.validation.constraints.Max.message}", violations2.iterator().next().getMessageTemplate());
    }
}