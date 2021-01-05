package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> result = memberRepository.findByName(member.getName());
        if (!result.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    public Member find(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id);
        member.setName(name);
    }
}
