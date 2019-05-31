/*
 * Copyright (c) 2019 Isaak Malik <isaakm@pm.me>
 *
 * Licensed as open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 * See LICENSE, LICENTIE or for other translations: https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12
 */

package click.wheredoi.secretshareapi.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "secrets")
public class Secret {
    @Id
    private String id;

    private String data;

    @Column(insertable = false)
    @Generated(GenerationTime.INSERT)
    private Timestamp created;

    private Timestamp expires;

    @OneToMany(mappedBy = "secret")
    private Set<AccessRecord> accessRecords;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getExpires() {
        return expires;
    }

    public void setExpires(Timestamp expires) {
        this.expires = expires;
    }

    public Set<AccessRecord> getAccessRecords() {
        return accessRecords;
    }

    public void setAccessRecords(Set<AccessRecord> accessRecords) {
        this.accessRecords = accessRecords;
    }
}
