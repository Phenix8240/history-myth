package com.myth.stories.service;

import com.myth.stories.entity.*;
import com.myth.stories.dto.DeityDtos.*;
import com.myth.stories.entity.Deity;
import com.myth.stories.entity.DetailType;
import com.myth.stories.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeityService {

    private final DeityRepository deityRepo;
    private final DeityStoryRepository storyRepo;
    private final DeityDetailRepository detailRepo;
    private final DeityRelationshipRepository relationshipRepo;

    // ---------- Read ----------

    public List<DeityListItem> list(String category) {
        List<Deity> deities = (category == null || category.isBlank())
                ? deityRepo.findAllByOrderByOrderNumberAscNameAsc()
                : deityRepo.findByCategoryIgnoreCaseOrderByOrderNumberAscNameAsc(category);

        return deities.stream()
                .map(d -> new DeityListItem(d.getId(), d.getName(), d.getSlug(),
                        d.getType().name(), d.getCategory(), d.getTradition(),
                        d.getShortDescription(), d.getThumbnailUrl()))
                .toList();
    }

    public DeityResponse getBySlug(String slug) {
        Deity d = findBySlug(slug);
        return new DeityResponse(
                d.getId(), d.getName(), d.getSlug(), d.getEnglishName(), d.getSanskritName(),
                d.getType().name(), d.getShortDescription(), d.getDescription(),
                d.getTradition(), d.getCategory(), d.getOrderNumber(),
                d.getPrimaryRole(), d.getDivinePower(), d.getSpiritualMeaning(), d.getTeachings(),
                d.getComplexion(), d.getNumberOfArms(), d.getPosture(), d.getAppearance(),
                d.getMantra(), d.getImportantTithi(), d.getMainFestival(), d.getWorshipDescription(),
                d.getImageUrl(), d.getThumbnailUrl(),
                storiesOf(d), detailsOf(d, null), relationshipsOf(d));
    }

    public List<StoryResponse> getStories(String slug) {
        return storiesOf(findBySlug(slug));
    }

    public List<DetailResponse> getDetails(String slug, DetailType type) {
        return detailsOf(findBySlug(slug), type);
    }

    public List<RelationshipResponse> getRelationships(String slug) {
        return relationshipsOf(findBySlug(slug));
    }

    // ---------- Internal loaders ----------

    private List<StoryResponse> storiesOf(Deity deity) {
        return storyRepo.findByDeityIdOrderByDisplayOrderAsc(deity.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    private List<DetailResponse> detailsOf(Deity deity, DetailType type) {
        var details = (type == null)
                ? detailRepo.findByDeityIdOrderByDisplayOrderAsc(deity.getId())
                : detailRepo.findByDeityIdAndTypeOrderByDisplayOrderAsc(deity.getId(), type);
        return details.stream().map(this::toResponse).toList();
    }

    private List<RelationshipResponse> relationshipsOf(Deity deity) {
        List<RelationshipResponse> result = new ArrayList<>();

        relationshipRepo.findBySourceDeityId(deity.getId()).forEach(r ->
                result.add(new RelationshipResponse(r.getId(), r.getRelationshipType().name(),
                        "OUTGOING", r.getTargetDeity().getName(),
                        r.getTargetDeity().getSlug(), r.getDescription())));

        relationshipRepo.findByTargetDeityId(deity.getId()).forEach(r ->
                result.add(new RelationshipResponse(r.getId(), r.getRelationshipType().name(),
                        "INCOMING", r.getSourceDeity().getName(),
                        r.getSourceDeity().getSlug(), r.getDescription())));

        return result;
    }
    // ---------- Write ----------

    @Transactional
    public Deity create(Deity deity) {
        if (deityRepo.existsBySlug(deity.getSlug())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists: " + deity.getSlug());
        }
        deity.setId(null);
        return deityRepo.save(deity);
    }

    @Transactional
    public Deity update(Long id, Deity incoming) {
        Deity existing = deityRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Deity not found: " + id));

        BeanUtils.copyProperties(incoming, existing,
                "id", "stories", "details", "relationships");
        return deityRepo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!deityRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deity not found: " + id);
        }
        deityRepo.deleteById(id);
    }

    // ---------- Helper ----------

    private Deity findBySlug(String slug) {
        return deityRepo.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Deity not found: " + slug));
    }

    // ---------- Stories ----------

    @Transactional
    public StoryResponse addStory(String slug, StoryRequest req) {
        Deity deity = findBySlug(slug);
        requireText(req.title(), "title");

        DeityStory story = DeityStory.builder()
                .title(req.title())
                .summary(req.summary())
                .fullStory(req.fullStory())
                .source(req.source())
                .displayOrder(req.displayOrder())
                .build();
        deity.addStory(story);
        return toResponse(storyRepo.save(story));
    }

    @Transactional
    public StoryResponse updateStory(String slug, Long storyId, StoryRequest req) {
        Deity deity = findBySlug(slug);
        DeityStory story = storyRepo.findById(storyId)
                .filter(s -> s.getDeity().getId().equals(deity.getId()))
                .orElseThrow(() -> notFound("Story not found: " + storyId));
        requireText(req.title(), "title");

        story.setTitle(req.title());
        story.setSummary(req.summary());
        story.setFullStory(req.fullStory());
        story.setSource(req.source());
        story.setDisplayOrder(req.displayOrder());
        return toResponse(storyRepo.save(story));
    }

    @Transactional
    public void deleteStory(String slug, Long storyId) {
        Deity deity = findBySlug(slug);
        DeityStory story = storyRepo.findById(storyId)
                .filter(s -> s.getDeity().getId().equals(deity.getId()))
                .orElseThrow(() -> notFound("Story not found: " + storyId));
        deity.getStories().remove(story);   // orphanRemoval deletes it
    }

    // ---------- Details ----------

    @Transactional
    public DetailResponse addDetail(String slug, DetailRequest req) {
        Deity deity = findBySlug(slug);
        validateDetail(req);

        DeityDetail detail = DeityDetail.builder()
                .type(req.type())
                .name(req.name())
                .description(req.description())
                .symbolism(req.symbolism())
                .handPosition(req.handPosition())
                .imageUrl(req.imageUrl())
                .displayOrder(req.displayOrder())
                .build();
        deity.addDetail(detail);
        return toResponse(detailRepo.save(detail));
    }

    @Transactional
    public DetailResponse updateDetail(String slug, Long detailId, DetailRequest req) {
        Deity deity = findBySlug(slug);
        DeityDetail detail = detailRepo.findById(detailId)
                .filter(d -> d.getDeity().getId().equals(deity.getId()))
                .orElseThrow(() -> notFound("Detail not found: " + detailId));
        validateDetail(req);

        detail.setType(req.type());
        detail.setName(req.name());
        detail.setDescription(req.description());
        detail.setSymbolism(req.symbolism());
        detail.setHandPosition(req.handPosition());
        detail.setImageUrl(req.imageUrl());
        detail.setDisplayOrder(req.displayOrder());
        return toResponse(detailRepo.save(detail));
    }

    @Transactional
    public void deleteDetail(String slug, Long detailId) {
        Deity deity = findBySlug(slug);
        DeityDetail detail = detailRepo.findById(detailId)
                .filter(d -> d.getDeity().getId().equals(deity.getId()))
                .orElseThrow(() -> notFound("Detail not found: " + detailId));
        deity.getDetails().remove(detail);  // orphanRemoval deletes it
    }

    // ---------- Relationships ----------

    @Transactional
    public RelationshipResponse addRelationship(String slug, RelationshipRequest req) {
        Deity source = findBySlug(slug);
        requireText(req.targetSlug(), "targetSlug");
        if (req.relationshipType() == null) {
            throw badRequest("relationshipType is required");
        }
        Deity target = findBySlug(req.targetSlug());
        if (source.getId().equals(target.getId())) {
            throw badRequest("A deity cannot be related to itself");
        }
        boolean exists = relationshipRepo.findBySourceDeityId(source.getId()).stream()
                .anyMatch(r -> r.getTargetDeity().getId().equals(target.getId())
                        && r.getRelationshipType() == req.relationshipType());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Relationship already exists");
        }

        DeityRelationship rel = DeityRelationship.builder()
                .sourceDeity(source)
                .targetDeity(target)
                .relationshipType(req.relationshipType())
                .description(req.description())
                .build();
        rel = relationshipRepo.save(rel);
        return new RelationshipResponse(rel.getId(), rel.getRelationshipType().name(),
                "OUTGOING", target.getName(), target.getSlug(), rel.getDescription());
    }

    @Transactional
    public void deleteRelationship(String slug, Long relationshipId) {
        Deity deity = findBySlug(slug);
        DeityRelationship rel = relationshipRepo.findById(relationshipId)
                .filter(r -> r.getSourceDeity().getId().equals(deity.getId())
                        || r.getTargetDeity().getId().equals(deity.getId()))
                .orElseThrow(() -> notFound("Relationship not found: " + relationshipId));
        relationshipRepo.delete(rel);
    }

    // ---------- Mapping / validation helpers ----------

    private StoryResponse toResponse(DeityStory s) {
        return new StoryResponse(s.getId(), s.getTitle(), s.getSummary(),
                s.getFullStory(), s.getSource(), s.getDisplayOrder());
    }

    private DetailResponse toResponse(DeityDetail d) {
        return new DetailResponse(d.getId(), d.getType().name(), d.getName(),
                d.getDescription(), d.getSymbolism(), d.getHandPosition(), d.getImageUrl());
    }

    private void validateDetail(DetailRequest req) {
        if (req.type() == null) throw badRequest("type is required");
        requireText(req.name(), "name");
    }

    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) throw badRequest(field + " is required");
    }

    private ResponseStatusException badRequest(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }

    private ResponseStatusException notFound(String msg) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
    }
}