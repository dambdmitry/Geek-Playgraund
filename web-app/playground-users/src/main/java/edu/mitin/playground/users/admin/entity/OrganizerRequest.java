package edu.mitin.playground.users.admin.entity;

import edu.mitin.playground.users.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "organizer_request")
public class OrganizerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User account;

    private String justification;

    public OrganizerRequest(User account, String justification) {
        this.account = account;
        this.justification = justification;
    }

    public OrganizerRequest() {
    }

    public Long getId() {
        return id;
    }

    public User getAccount() {
        return account;
    }

    public void setAccount(User requester) {
        this.account = requester;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getJustification() {
        return justification;
    }
}
