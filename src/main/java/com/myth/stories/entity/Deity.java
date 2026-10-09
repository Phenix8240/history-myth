package com.myth.stories.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deities", indexes = {
        @Index(name = "idx_deity_slug", columnList = "slug"),
        @Index(name = "idx_deity_category", columnList = "category")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Deity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---------- Basic ----------
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String englishName;
    private String sanskritName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeityType type;

    @Column(length = 1000)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    // ---------- Classification ----------
    private String tradition;      // Shakta, Shaiva, Vaishnava
    private String category;       // Mahavidya, Avatar, Main Deity
    private Integer orderNumber;

    // ---------- Divine ----------
    private String primaryRole;
    private String divinePower;

    @Column(columnDefinition = "TEXT")
    private String spiritualMeaning;   // merges symbolism + spiritualMeaning

    @Column(columnDefinition = "TEXT")
    private String teachings;

    // ---------- Iconography (summary) ----------
    private String complexion;
    private Integer numberOfArms;
    private String posture;

    @Column(columnDefinition = "TEXT")
    private String appearance;         // clothing/ornaments detail goes here or in DeityDetail

    // ---------- Worship ----------
    private String mantra;             // primary mantra; extra ones go in DeityDetail
    private String importantTithi;
    private String mainFestival;

    @Column(columnDefinition = "TEXT")
    private String worshipDescription;

    // ---------- Media ----------
    private String imageUrl;
    private String thumbnailUrl;

    // ---------- Relationships ----------
    @OneToMany(mappedBy = "deity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<DeityStory> stories = new ArrayList<>();

    @OneToMany(mappedBy = "deity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<DeityDetail> details = new ArrayList<>();

    // Deity -> others (this deity is the source)
    @OneToMany(mappedBy = "sourceDeity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeityRelationship> relationships = new ArrayList<>();

    // ---------- Helpers ----------
    public void addStory(DeityStory story) {
        stories.add(story);
        story.setDeity(this);
    }

    public void addDetail(DeityDetail detail) {
        details.add(detail);
        detail.setDeity(this);
    }

    public void addRelationship(Deity target, RelationshipType type, String description) {
        relationships.add(DeityRelationship.builder()
                .sourceDeity(this)
                .targetDeity(target)
                .relationshipType(type)
                .description(description)
                .build());
    }
}