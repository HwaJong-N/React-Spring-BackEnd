package ghkwhd.apiServer.member.repository;

import ghkwhd.apiServer.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {
    @EntityGraph(attributePaths = "memberRoleList")
    @Query("select m from Member m where m.email = :email")
    Member findMemberWithRoles(@Param("email") String email);
}
