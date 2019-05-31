/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.service;

import click.wheredoi.secretshareapi.model.AccessRecord;
import click.wheredoi.secretshareapi.repo.AccessRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AccessService {

    private final AccessRepository accessRepository;

    public AccessService(AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    /**
     * Keep a record of access history
     *
     * @param secretId NanoId of secret
     * @param request Request data object
     */
    public void addAccessRecord(String secretId, HttpServletRequest request) {
        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setSecret(secretId);
        accessRecord.setIp(request.getRemoteAddr());

        accessRepository.save(accessRecord);
    }
}
