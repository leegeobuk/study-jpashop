package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void joinTest() {
        //given
        Member member = new Member();
        member.setName("Kim");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findById(savedId));
    }

    @Test
    public void validateDuplicateMemberTest() {
        //given
        Member member1 = new Member();
        member1.setName("Kim");
        Member member2 = new Member();
        member2.setName("Kim");

        //when
        memberService.join(member1);

        //then
        assertThrows(Exception.class, () -> memberService.join(member2), "예외가 발생해야 한다!");
    }
}