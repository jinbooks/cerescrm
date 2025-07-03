<template>
  <div class="app-container" v-loading="submitLoading">
    <el-card class="common-card detail-header">
      <el-divider><span>商机基本信息</span></el-divider>
      <el-descriptions :column="4" border label-class-name="label-style" style="margin-top: 15px">
        <el-descriptions-item label="商机编号">{{ opportunity.opportunityCode }}</el-descriptions-item>
        <el-descriptions-item label="商机名称">{{ opportunity.name }}</el-descriptions-item>
        <el-descriptions-item label="商机描述">
          <el-tooltip class="item" effect="dark" :content="opportunity.description" placement="top">
            <span class="ellipsis">{{ opportunity.description}}</span>
          </el-tooltip>
        </el-descriptions-item>
        <el-descriptions-item label="客户">{{ opportunity.customerName || "无"}}</el-descriptions-item>

        <el-descriptions-item label="联系人">{{ opportunity.contactName || "无"}}</el-descriptions-item>
        <el-descriptions-item label="商机类型">{{ getTypeLabel(opportunity.type) }}</el-descriptions-item>
        <el-descriptions-item label="来源类型">{{ getSourceTypeLabel(opportunity.sourceType) }}</el-descriptions-item>
        <el-descriptions-item label="来源线索" v-if=" opportunity.sourceType === 1">{{ opportunity.leadName || "无"}}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ opportunity.peopleName || "无"}}</el-descriptions-item>
        <el-descriptions-item label="公司">{{ opportunity.company || "无"}}</el-descriptions-item>

        <el-descriptions-item label="来源详情">
          <el-tooltip class="item" effect="dark" :content="opportunity.sourceDetail" placement="top">
            <span class="ellipsis">{{ opportunity.sourceDetail}}</span>
          </el-tooltip>
        </el-descriptions-item>
        <el-descriptions-item label="行业">{{ getIndustryLabel(opportunity.industry) }}</el-descriptions-item>
        <el-descriptions-item label="所在地">{{ opportunity.province }}{{opportunity.city}}</el-descriptions-item>
        <el-descriptions-item label="商机金额">{{ formatAmount(opportunity.amount) }}</el-descriptions-item>

        <el-descriptions-item label="预期金额">{{ formatAmount(opportunity.expectedAmount) }}</el-descriptions-item>
        <el-descriptions-item label="加权金额">{{ formatAmount(opportunity.weightedAmount) }}</el-descriptions-item>
        <el-descriptions-item label="预估成本">{{ formatAmount(opportunity.cost) }}</el-descriptions-item>
        <el-descriptions-item label="预估利润">{{ formatAmount(opportunity.profit) }}</el-descriptions-item>

        <el-descriptions-item label="当前阶段">{{ opportunity.stageName }}</el-descriptions-item>
        <el-descriptions-item label="成功概率">{{ opportunity.probability || 0}}%</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusLabel(opportunity.status) }}</el-descriptions-item>
        <el-descriptions-item label="赢单原因" v-if="opportunity.status === 2">
          <el-tooltip class="item" effect="dark" :content="opportunity.winReason" placement="top">
            <span class="ellipsis">{{ opportunity.winReason}}</span>
          </el-tooltip>
        </el-descriptions-item>
        <el-descriptions-item label="输单原因" v-if="opportunity.status === 3">
          <el-tooltip class="item" effect="dark" :content="opportunity.loseReason" placement="top">
            <span class="ellipsis">{{ opportunity.loseReason}}</span>
          </el-tooltip>
        </el-descriptions-item>

        <el-descriptions-item label="预计成交日期">{{ opportunity.expectedCloseDate }}</el-descriptions-item>
        <el-descriptions-item label="实际成交日期">{{ opportunity.actualCloseDate }}</el-descriptions-item>
        <el-descriptions-item label="首次接触日期">{{ opportunity.firstContactDate }}</el-descriptions-item>
        <el-descriptions-item label="最后活动日期">{{ opportunity.lastActivityDate }}</el-descriptions-item>

        <el-descriptions-item label="负责人">{{ opportunity.ownerName || "无"}}</el-descriptions-item>
        <el-descriptions-item label="分配时间">{{ opportunity.assignedAt }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ getPriorityLabel(opportunity.priority) }}</el-descriptions-item>
        <el-descriptions-item label="质量评分">{{ opportunity.qualityScore }}</el-descriptions-item>
      </el-descriptions>

    </el-card>
    <el-card class="common-card detail-header">
      <div class="section-title">竞争对手</div>
      <div>
        <editor v-model="opportunity.competitors" :height="200" read-only />
      </div>
    </el-card>
    <el-card class="common-card detail-header">
      <div class="section-title-follow">跟进记录</div>
      <div v-for="(item, index) in followUps" :key="index" class="follow-record">
        <div class="left-border">
          <div class="follow-title">{{ getFollowTypeLabel(item.followType) }}</div>
          <div class="follow-desc">{{ item.content }}</div>
          <el-tag type="success" size="small">{{ getResultsLabel(item.result) }}</el-tag>
        </div>
        <div class="follow-time">跟进时间：{{ item.followAt }}</div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">

import {getCurrentInstance, onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import {formatAmount} from "../../utils";
import Editor from "@/components/Editor/index.vue";
import {getOne} from "@/api/opportunity/opportunity";
import {createValueLabelMap, getLabelFromMap} from "@/utils/Dict-utils";
const {proxy} = getCurrentInstance()!;
const {opportunity_type, opportunity_source_type, opportunity_status, priority, books_industry, follow_types, results}
    = proxy?.useDict("opportunity_type", "opportunity_source_type", "opportunity_status", "priority", "books_industry",
"follow_types", "results");
//行业
const flatIndustryList = books_industry.value.flatMap(group => group.options)
const industryMap = createValueLabelMap(flatIndustryList)
const getIndustryLabel = (val: number) => getLabelFromMap(industryMap, val)
//状态
const statusMap = createValueLabelMap(opportunity_status.value)
const getStatusLabel = (val: number) => getLabelFromMap(statusMap, val)
//类型
const typeMap = createValueLabelMap(opportunity_type.value)
const getTypeLabel = (val: number) => getLabelFromMap(typeMap, val)
//来源类型
const sourceTypeMap = createValueLabelMap(opportunity_source_type.value)
const getSourceTypeLabel = (val: number) => getLabelFromMap(sourceTypeMap, val)
//优先级
const priorityMap = createValueLabelMap(priority.value)
const getPriorityLabel = (val: number) => getLabelFromMap(priorityMap, val)
//跟进类型
const followTypesMap = createValueLabelMap(follow_types.value)
const getFollowTypeLabel = (val: number) => getLabelFromMap(followTypesMap, val)
//跟进结果
const resultsMap = createValueLabelMap(results.value)
const getResultsLabel = (val: number) => getLabelFromMap(resultsMap, val)

const router = useRouter()
const submitLoading = ref(true)

// 获取路由参数
const oppId = router.currentRoute.value.query.id as string

// 客户基础信息
const opportunity = ref<any>({})
//跟进记录
const followUps = ref<any>([])

function loadDetails() {
  if (!oppId) return
  submitLoading.value = true;
  getOne(oppId).then((res: any) => {
    opportunity.value = res.data.opportunity
    followUps.value = res.data.followUps

    submitLoading.value = false
  })
}

onMounted(() => {
  loadDetails()
})
</script>

<style scoped>
.section-title {
  font-size: 16px;
  font-weight: bold;
  border-left: 4px solid orangered;
  padding-left: 8px;
  margin-bottom: 16px;
}

.section-title-follow {
  font-size: 16px;
  font-weight: bold;
  color: #2d5cf6;
  border-left: 4px solid #2d5cf6;
  padding-left: 8px;
  margin-bottom: 16px;
}

.follow-record {
  position: relative;
  display: flex;
  justify-content: space-between;
  padding: 16px;
  border-radius: 8px;
  background-color: #f9f9fb;
  margin-bottom: 12px;
}

.left-border {
  border-left: 4px solid #2d5cf6;
  padding-left: 12px;
  flex: 1;
}

.follow-title {
  font-weight: 600;
  font-size: 15px;
  margin-bottom: 6px;
  color: #333;
}

.follow-desc {
  margin-bottom: 8px;
  font-size: 14px;
  color: #555;
}

.follow-time {
  font-size: 13px;
  color: #999;
  white-space: nowrap;
  margin-left: 16px;
  align-self: flex-start;
}

</style>
