package com.myth.stories.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deity_temples")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeityTemple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deity_id", nullable = false)
    private Deity deity;

    @Column(nullable = false)
    private String name;

    private String city;

    private String state;

    private String country;

    private Double latitude;

    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;
}