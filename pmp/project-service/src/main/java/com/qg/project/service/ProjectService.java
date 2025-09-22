package com.qg.project.service;


import com.qg.common.domain.po.Result;
import com.qg.project.domain.dto.InviteDto;
import com.qg.project.domain.po.Project;
import com.qg.project.domain.vo.PersonalProjectVO;


public interface ProjectService {
     Result addProject(PersonalProjectVO personalProjectVO);

     Result updateProject(Project project);

     Result deleteProject(String uuid);

     Result getProject(String uuid);

     Result getPublicProjectList();

     Result getPersonalProject(Long userId);

//     Result getPersonalUnpublicProject(Long userId);

    Result getInviteDCode(String projectId);

     Result joinProject(InviteDto inviteDto);

    Result selectProjectByName(String name);

    Result getPrivateProjectList();

    boolean checkProjectIdExist(String projectId);

    String selectWebhookByProjectId(String projectId);
}
