package com.myth.stories.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deity_stories")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DeityStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deity_id", nullable = false)
    private Deity deity;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String fullStory;

    private String source;          // Devi Mahatmyam, Skanda Purana...

    private Integer displayOrder;
}