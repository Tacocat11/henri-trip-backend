package com.henritrip.backend.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "guides")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer numberOfDays;

    @ElementCollection(targetClass = Mobility.class)
    @CollectionTable(
        name = "guide_mobilities",
        joinColumns = @JoinColumn(name = "guide_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "mobility", nullable = false)
    private Set<Mobility> mobilities = new HashSet<>();

    @ElementCollection(targetClass = Season.class)
    @CollectionTable(
        name = "guide_seasons",
        joinColumns = @JoinColumn(name = "guide_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "season", nullable = false)
    private Set<Season> seasons = new HashSet<>();

    @ElementCollection(targetClass = Audience.class)
    @CollectionTable(
        name = "guide_audiences",
        joinColumns = @JoinColumn(name = "guide_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "audience", nullable = false)
    private Set<Audience> audiences = new HashSet<>();

    public Guide() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
    	this.id = id;
    	
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public Set<Mobility> getMobilities() {
        return mobilities;
    }

    public void setMobilities(Set<Mobility> mobilities) {
        this.mobilities = mobilities;
    }

    public Set<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(Set<Season> seasons) {
        this.seasons = seasons;
    }

    public Set<Audience> getAudiences() {
        return audiences;
    }

    public void setAudiences(Set<Audience> audiences) {
        this.audiences = audiences;
    }

}