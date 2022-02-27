package edu.mitin.playground.users.admin.model;

import edu.mitin.playground.users.model.User;

import javax.persistence.*;

@Entity
public class OrganizerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn()
    private User requester;

    private String justification;

    public OrganizerRequest(User requester, String justification) {
        this.requester = requester;
        this.justification = justification;
    }

    public OrganizerRequest() {
    }

    public Long getId() {
        return id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getJustification() {
        return justification;
    }
}
