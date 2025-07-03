package com.jinbooks.web.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.customer.CustomerSegment;
import com.jinbooks.entity.customer.dto.CustomerSegmentPageDto;
import com.jinbooks.entity.customer.vo.CustomerSegmentVo;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.service.CustomerSegmentService;
import com.jinbooks.validate.AddGroup;
import com.jinbooks.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 客户分层控制器
 *
 * @author Wuyan
 */
@RestController
@RequestMapping("/customer/segment")
@RequiredArgsConstructor
@Slf4j
public class CustomerSegmentController {

    private final CustomerSegmentService segmentService;

    /**
     * 分页查询客户分层列表
     */
    @GetMapping("/fetch")
    public Message<IPage<CustomerSegmentVo>> fetch(@ParameterObject CustomerSegmentPageDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(segmentService.pageList(dto));
    }

    /**
     * 获取客户分层详情
     */
    @GetMapping("/get/{id}")
    public Message<CustomerSegmentVo> getById(@PathVariable("id") String id) {
        log.debug("get {}", id);
        return new Message<>(Message.SUCCESS, segmentService.getById(id));
    }

    /**
     * 新增客户分层
     */
    @PostMapping("/save")
    public Message<Boolean> save(@Validated(AddGroup.class) @RequestBody CustomerSegment dto,@CurrentUser UserInfo currentUser) {
        log.debug("save {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(segmentService.save(dto));
    }

    /**
     * 更新客户分层
     */
    @PutMapping("/update")
    public Message<Boolean> update(@Validated(EditGroup.class) @RequestBody CustomerSegment dto,@CurrentUser UserInfo currentUser) {
        log.debug("update {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(segmentService.update(dto));
    }

    /**
     * 删除客户分层
     */
    @DeleteMapping("/delete")
    public Message<Boolean> delete(@Validated @RequestBody ListIdsDto dto,@CurrentUser UserInfo currentUser) {
        log.debug("delete {}", dto);
        dto.setWorkspaceId(currentUser.getWorkspaceId());
        return Message.ok(segmentService.deleteByIds(dto.getListIds()));
    }
}
