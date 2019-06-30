/*
 * Copyright (c) 2019 Leonardo Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi;

import click.wheredoi.secretshareapi.service.SecretService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
public class ScheduledTasks {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SecretService secretService;

    public ScheduledTasks(SecretService secretService) {
        this.secretService = secretService;
    }

    // Each hour
    @Scheduled(cron = "0 0 * * * *")
    public void cronDeleteExpiredSecrets() {
        secretService.deleteExpiredSecrets();
        logger.info("Cron job cronDeleteExpiredSecrets() has been executed");
    }
}
