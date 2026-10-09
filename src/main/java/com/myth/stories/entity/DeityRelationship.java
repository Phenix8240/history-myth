package com.myth.stories.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deity_relationships",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"source_deity_id", "target_deity_id", "relationshipType"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DeityRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_deity_id", nullable = false)
    private Deity sourceDeity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_deity_id", nullable = false)
    private Deity targetDeity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipType relationshipType;

    @Column(columnDefinition = "TEXT")
    private String description;
}