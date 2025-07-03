package com.jinbooks.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.dto.ListIdsDto;
import com.jinbooks.entity.opportunity.OppStage;
import com.jinbooks.entity.opportunity.dto.OppStageChangeDto;
import com.jinbooks.entity.opportunity.dto.OppStagePageDto;

import java.util.List;

public interface OppStageService extends IService<OppStage> {
    Message<OppStage> getById(String id);

    Message<Page<OppStage>> pageList(OppStagePageDto dto);

    Message<String> save(OppStageChangeDto dto);

    Message<String> update(OppStageChangeDto dto);

    Message<String> delete(ListIdsDto dto);

    Message<List<OppStage>> getAll(OppStage oppStage);
}
