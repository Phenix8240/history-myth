package com.myth.stories.dto;

import com.myth.stories.entity.DetailType;
import com.myth.stories.entity.RelationshipType;

import java.util.List;

public class DeityDtos {

    public record DeityListItem(
            Long id, String name, String slug, String type,
            String category, String tradition,
            String shortDescription, String thumbnailUrl) {}

    public record StoryResponse(
            Long id, String title, String summary,
            String fullStory, String source, Integer displayOrder) {}

    public record DetailResponse(
            Long id, String type, String name, String description,
            String symbolism, String handPosition, String imageUrl) {}

    public record RelationshipResponse(
            Long id, String relationshipType, String direction,
            String deityName, String deitySlug, String description) {}

    public record DeityResponse(
            Long id, String name, String slug, String englishName, String sanskritName,
            String type, String shortDescription, String description,
            String tradition, String category, Integer orderNumber,
            String primaryRole, String divinePower, String spiritualMeaning, String teachings,
            String complexion, Integer numberOfArms, String posture, String appearance,
            String mantra, String importantTithi, String mainFestival, String worshipDescription,
            String imageUrl, String thumbnailUrl,
            List<StoryResponse> stories,
            List<DetailResponse> details,
            List<RelationshipResponse> relationships) {}

    // ---------- Requests ----------

    public record StoryRequest(
            String title, String summary, String fullStory,
            String source, Integer displayOrder) {}

    public record DetailRequest(
            DetailType type, String name, String description,
            String symbolism, String handPosition, String imageUrl,
            Integer displayOrder) {}

    public record RelationshipRequest(
            String targetSlug, RelationshipType relationshipType, String description) {}
}