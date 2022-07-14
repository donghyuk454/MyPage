package com.mong.MyProject.domain.image.user;

import com.mong.MyProject.domain.image.Image;
import com.mong.MyProject.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "member_image")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class MemberImage extends Image {
    @OneToOne
    @JoinColumn(name="member_id", nullable = false, updatable = false)
    private Member member;
}
