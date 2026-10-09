package com.myth.stories.controller;

import com.myth.stories.dto.DeityDtos.*;
import com.myth.stories.entity.Deity;
import com.myth.stories.entity.DetailType;
import com.myth.stories.service.DeityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deities")
@RequiredArgsConstructor
public class DeityController {

    private final DeityService service;

    // GET /api/deities?category=Mahavidya
    @GetMapping
    public List<DeityListItem> list(@RequestParam(required = false) String category) {
        return service.list(category);
    }

    // GET /api/deities/kali
    // GET /api/deities/kali
    @GetMapping("/{slug}")
    public DeityResponse get(@PathVariable String slug) {
        return service.getBySlug(slug);
    }

    // GET /api/deities/kali/stories
    @GetMapping("/{slug}/stories")
    public List<StoryResponse> stories(@PathVariable String slug) {
        return service.getStories(slug);
    }

    // GET /api/deities/kali/details?type=WEAPON
    @GetMapping("/{slug}/details")
    public List<DetailResponse> details(@PathVariable String slug,
                                        @RequestParam(required = false) DetailType type) {
        return service.getDetails(slug, type);
    }

    // GET /api/deities/kali/relationships
    @GetMapping("/{slug}/relationships")
    public List<RelationshipResponse> relationships(@PathVariable String slug) {
        return service.getRelationships(slug);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Deity create(@RequestBody Deity deity) {
        return service.create(deity);
    }

    @PutMapping("/{id}")
    public Deity update(@PathVariable Long id, @RequestBody Deity deity) {
        return service.update(id, deity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


    // ---------- Stories ----------

    // POST /api/deities/kali/stories
    @PostMapping("/{slug}/stories")
    @ResponseStatus(HttpStatus.CREATED)
    public StoryResponse addStory(@PathVariable String slug, @RequestBody StoryRequest req) {
        return service.addStory(slug, req);
    }

    // PUT /api/deities/kali/stories/5
    @PutMapping("/{slug}/stories/{storyId}")
    public StoryResponse updateStory(@PathVariable String slug, @PathVariable Long storyId,
                                     @RequestBody StoryRequest req) {
        return service.updateStory(slug, storyId, req);
    }

    // DELETE /api/deities/kali/stories/5
    @DeleteMapping("/{slug}/stories/{storyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStory(@PathVariable String slug, @PathVariable Long storyId) {
        service.deleteStory(slug, storyId);
    }

    // ---------- Details ----------

    // POST /api/deities/kali/details
    @PostMapping("/{slug}/details")
    @ResponseStatus(HttpStatus.CREATED)
    public DetailResponse addDetail(@PathVariable String slug, @RequestBody DetailRequest req) {
        return service.addDetail(slug, req);
    }

    // PUT /api/deities/kali/details/3
    @PutMapping("/{slug}/details/{detailId}")
    public DetailResponse updateDetail(@PathVariable String slug, @PathVariable Long detailId,
                                       @RequestBody DetailRequest req) {
        return service.updateDetail(slug, detailId, req);
    }

    // DELETE /api/deities/kali/details/3
    @DeleteMapping("/{slug}/details/{detailId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDetail(@PathVariable String slug, @PathVariable Long detailId) {
        service.deleteDetail(slug, detailId);
    }

    // ---------- Relationships ----------

    // POST /api/deities/kali/relationships
    @PostMapping("/{slug}/relationships")
    @ResponseStatus(HttpStatus.CREATED)
    public RelationshipResponse addRelationship(@PathVariable String slug,
                                                @RequestBody RelationshipRequest req) {
        return service.addRelationship(slug, req);
    }

    // DELETE /api/deities/kali/relationships/2
    @DeleteMapping("/{slug}/relationships/{relationshipId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRelationship(@PathVariable String slug, @PathVariable Long relationshipId) {
        service.deleteRelationship(slug, relationshipId);
    }
}