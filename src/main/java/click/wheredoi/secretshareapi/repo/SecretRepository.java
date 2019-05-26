/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.repo;


import click.wheredoi.secretshareapi.model.Secret;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface SecretRepository extends CrudRepository<Secret, String> {

    @Transactional
    @Modifying
    @Query(value = "DELETE from secrets s WHERE s.expires < NOW()", nativeQuery = true)
    void deleteExpired();
}
