/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.repo;

import click.wheredoi.secretshareapi.model.AccessRecord;
import org.springframework.data.repository.CrudRepository;

public interface AccessRepository extends CrudRepository<AccessRecord, Integer> {
}
