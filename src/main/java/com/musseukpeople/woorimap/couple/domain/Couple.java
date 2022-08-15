package com.musseukpeople.woorimap.couple.domain;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.musseukpeople.woorimap.common.model.BaseEntity;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.tag.domain.Tag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Embedded
    private CoupleMembers coupleMembers;

    @OneToMany(mappedBy = "couple", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "couple", orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    public Couple(LocalDate startDate, CoupleMembers coupleMembers) {
        this(null, startDate, coupleMembers);
    }

    public Couple(Long id, LocalDate startDate, CoupleMembers coupleMembers) {
        checkArgument(startDate.isBefore(LocalDate.now().plusDays(1)),
            "현재 이후 날짜로 커플을 생성할 수 없습니다.");

        this.id = id;
        this.startDate = startDate;
        this.coupleMembers = coupleMembers;
        this.coupleMembers.assignCouple(this);
    }

    public Member getMyMember(Long id) {
        return this.coupleMembers.getMyMember(id);
    }

    public Member getOpponentMember(Long id) {
        return this.coupleMembers.getOpponentMember(id);
    }

    public void changStartDate(LocalDate modifyDate) {
        checkArgument(modifyDate.isBefore(LocalDate.now().plusDays(1)),
            "현재 이후 날짜로 커플을 수정할 수 없습니다.");

        this.startDate = modifyDate;
    }

    public boolean isSame(Couple couple) {
        return this.id.equals(couple.id);
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
}
