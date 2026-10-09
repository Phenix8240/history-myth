package com.myth.stories.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "deity_weapons")
public class DeityWeapon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "deity_id")
    private Deity deity;

    private String name;

    private String description;

    private String symbolism;

    private String handPosition;
}