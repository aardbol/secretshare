/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi;

import click.wheredoi.secretshareapi.service.SecretService;
import org.awaitility.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

// Disabled because not possible to test the one hour cron job immediately
@Disabled
class ScheduledTasksTest {
    @Mock
    private ScheduledTasks tasks;

    @Mock
    private SecretService secretService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCronDeleteExpiredSecrets() {
        Mockito.doNothing().when(secretService).deleteExpiredSecrets();

        await().atMost(Duration.FIVE_SECONDS).untilAsserted(() ->
                verify(tasks, atLeast(1)).cronDeleteExpiredSecrets());
    }
}
