package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserDocumentId implements Serializable {
    private static final long serialVersionUID = -227416806969850403L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "document_id", nullable = false)
    private Integer documentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserDocumentId entity = (UserDocumentId) o;
        return Objects.equals(this.documentId, entity.documentId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, userId);
    }

}