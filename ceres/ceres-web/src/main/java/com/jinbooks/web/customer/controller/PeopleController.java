package com.jinbooks.web.customer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.customer.People;
import com.jinbooks.entity.customer.dto.PeoplePageDto;
import com.jinbooks.entity.customer.vo.PeopleVo;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.service.PeopleService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 联系人控制器
 *
 * @author Wuyan
 */
@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
@Slf4j
public class PeopleController {

    private final PeopleService peopleService;

    /**
     * 分页查询联系人列表
     */
    @GetMapping("/fetch")
    public Message<Page<PeopleVo>> fetch(@ParameterObject PeoplePageDto dto,@CurrentUser UserInfo currentUser) {
    	dto.setWorkspaceId(currentUser.getWorkspaceId());
    	log.debug("fetch {}", dto);
        return Message.ok(peopleService.pageList(dto));
    }

    /**
     * 获取联系人详情
     */
    @GetMapping("/get/{id}")
    public Message<PeopleVo> getById(@PathVariable("id") String id,@CurrentUser UserInfo currentUser) {
        log.debug("get {}", id);
        return new Message<>(Message.SUCCESS, peopleService.getById(id,currentUser.getWorkspaceId()));
    }

    /**
     * 新增联系人
     */
    @PostMapping("/save")
    public Message<Boolean> save(@Validated(AddGroup.class) @RequestBody People dto,@CurrentUser UserInfo currentUser) {
    	dto.setWorkspaceId(currentUser.getWorkspaceId());
    	log.debug("save {}", dto);
        return Message.ok(peopleService.save(dto));
    }

    /**
     * 更新联系人
     */
    @PutMapping("/update")
    public Message<Boolean> update(@Validated(EditGroup.class) @RequestBody People dto,@CurrentUser UserInfo currentUser) {
    	dto.setWorkspaceId(currentUser.getWorkspaceId());
    	log.debug("update {}", dto);
        return Message.ok(peopleService.update(dto));
    }

    /**
     * 删除联系人
     */
    @DeleteMapping("/delete")
    public Message<Boolean> delete(@Validated @RequestBody ListIdsDto dto,@CurrentUser UserInfo currentUser) {
    	dto.setWorkspaceId(currentUser.getWorkspaceId());
        log.debug("delete {}", dto);
        return Message.ok(peopleService.deleteByIds(dto.getListIds()));
    }
}
