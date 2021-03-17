package com.aditor.bi.commercials.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @Column
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    @JoinColumn()
    private User user;

    @Column
    private String offerId = "*";

    @Column
    private String mediaSource = "*";

    @Enumerated(EnumType.STRING)
    private ActionType action;

    public Permission() {
    }

    public Permission(String offerId, String mediaSource, ActionType action) {
        this.offerId = offerId;
        this.mediaSource = mediaSource;
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getMediaSource() {
        return mediaSource;
    }

    public void setMediaSource(String mediaSource) {
        this.mediaSource = mediaSource;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }
}
