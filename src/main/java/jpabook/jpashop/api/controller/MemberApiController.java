package jpabook.jpashop.api.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result<List<MemberDto>> membersV2() {
        List<Member> membersFound = memberService.findMembers();
        List<MemberDto> collected = membersFound.stream()
            .map(m -> new MemberDto(m.getName()))
            .collect(Collectors.toList());
        return new Result<>(collected);
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                             @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member member = memberService.find(id);
        return new UpdateMemberResponse(member.getId(), member.getName());
    }

    @Getter
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Getter
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @Getter
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Getter
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Getter
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Getter
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

}
