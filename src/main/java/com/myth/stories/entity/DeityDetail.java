package com.myth.stories.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deity_details", indexes = {
        @Index(name = "idx_detail_deity_type", columnList = "deity_id, type")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DeityDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deity_id", nullable = false)
    private Deity deity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DetailType type;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String symbolism;

    private String handPosition;   // from DeityWeapon (e.g. "upper right")

    private String imageUrl;

    private Integer displayOrder;
}