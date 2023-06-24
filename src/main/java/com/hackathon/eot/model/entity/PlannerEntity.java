package com.hackathon.eot.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "planner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlannerEntity extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "title")
    private String title;

    private PlannerEntity(UserAccount userAccount, String title) {
        this.user = userAccount;
        this.title = title;
    }

    public static PlannerEntity of(UserAccount userAccount, String title) {
        return new PlannerEntity(userAccount, title);
    }
}
