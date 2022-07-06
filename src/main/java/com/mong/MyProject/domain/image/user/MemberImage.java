package com.mong.MyProject.domain.image.user;

import com.mong.MyProject.domain.image.Image;
import com.mong.MyProject.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "member_image")
@Getter
@Setter
public class MemberImage extends Image {
    @OneToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;
}
