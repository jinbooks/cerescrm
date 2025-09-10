<template>
  <div class="app-container" v-loading="submitLoading">
    <el-card class="common-card detail-header">
      <el-divider><span>{{t('oppBasicInfo')}}</span></el-divider>
      <el-descriptions :column="4" border label-class-name="label-style" style="margin-top: 15px">
        <el-descriptions-item :label="t('oppName')">{{ opportunity.opportunityCode }}</el-descriptions-item>
        <el-descriptions-item :label="t('oppNo')">{{ opportunity.name }}</el-descriptions-item>
        <el-descriptions-item :label="t('oppDes')">
          <el-tooltip class="item" effect="dark" :content="opportunity.description" placement="top">
            <span class="ellipsis">{{ opportunity.description}}</span>
          </el-tooltip>
        </el-descriptions-item>
        <el-descriptions-item :label="t('subjectCustomer')">{{ opportunity.customerName || t('none')}}</el-descriptions-item>

        <el-descriptions-item :label="t('contact')">{{ opportunity.contactName || t('none')}}</el-descriptions-item>
        <el-descriptions-item :label="t('oppType')">{{ getTypeLabel(opportunity.type) }}</el-descriptions-item>
        <el-descriptions-item :label="t('SourceType')">{{ getSourceTypeLabel(opportunity.sourceType) }}</el-descriptions-item>
        <el-descriptions-item :label="t('sourceLead')" v-if=" opportunity.sourceType === 1">{{ opportunity.leadName || t('none')}}</el-descriptions-item>
        <el-descriptions-item :label="t('jbx.users.displayName')">{{ opportunity.peopleName || t('none')}}</el-descriptions-item>
        <el-descriptions-item :label="t('jbx.organizations.typeCompany')">{{ opportunity.company || t('none')}}</el-descriptions-item>

        <el-descriptions-item :label="t('SourceDetails')">
          <el-tooltip class="item" effect="dark" :content="opportunity.sourceDetail" placement="top">
            <span class="ellipsis">{{ opportunity.sourceDetail}}</span>
          </el-tooltip>
        </el-descriptions-item>
        <el-descriptions-item :label="t('industry')">{{ getIndustryLabel(opportunity.industry) }}</el-descriptions-item>
        <el-descriptions-item :label="t('Region')">{{ opportunity.province }}{{opportunity.city}}</el-descriptions-item>
        <el-descriptions-item :label="t('oppAmount')">{{ formatAmount(opportunity.amount) }}</el-descriptions-item>

        <el-descriptions-item :label="t('expAmount')">{{ formatAmount(opportunity.expectedAmount) }}</el-descriptions-item>
        <el-descriptions-item :label="t('weightAmount')">{{ formatAmount(opportunity.weightedAmount) }}</el-descriptions-item>
        <el-descriptions-item :label="t('expCost')">{{ formatAmount(opportunity.cost) }}</el-descriptions-item>
        <el-descriptions-item :label="t('estimateProfit')">{{ formatAmount(opportunity.profit) }}</el-descriptions-item>

        <el-descriptions-item :label="t('currentStage')">{{ opportunity.stageName }}</el-descriptions-item>
        <el-descriptions-item :label="t('successProb')">{{ opportunity.probability || 0}}%</el-descriptions-item>
        <el-descriptions-item :label="t('org.status')">{{ getStatusLabel(opportunity.status) }}</el-descriptions-item>
        <el-descriptions-item :label="t('winReason')" v-if="opportunity.status === 2">
          <el-tooltip class="item" effect="dark" :content="opportunity.winReason" placement="top">
            <span class="ellipsis">{{ opportunity.winReason}}</span>
          </el-tooltip>
        </el-descriptions-item>
        <el-descriptions-item :label="t('loseReason')" v-if="opportunity.status === 3">
          <el-tooltip class="item" effect="dark" :content="opportunity.loseReason" placement="top">
            <span class="ellipsis">{{ opportunity.loseReason}}</span>
          </el-tooltip>
        </el-descriptions-item>

        <el-descriptions-item :label="t('expClosingDate')">{{ opportunity.expectedCloseDate }}</el-descriptions-item>
        <el-descriptions-item :label="t('ActualTransactionDate')">{{ opportunity.actualCloseDate }}</el-descriptions-item>
        <el-descriptions-item :label="t('firstContactDate')">{{ opportunity.firstContactDate }}</el-descriptions-item>
        <el-descriptions-item :label="t('LastEventDate')">{{ opportunity.lastActivityDate }}</el-descriptions-item>

        <el-descriptions-item :label="t('PersonCharge')">{{ opportunity.ownerName || "无"}}</el-descriptions-item>
        <el-descriptions-item :label="t('AllocateTime')">{{ opportunity.assignedAt }}</el-descriptions-item>
        <el-descriptions-item :label="t('priority')">{{ getPriorityLabel(opportunity.priority) }}</el-descriptions-item>
        <el-descriptions-item :label="t('QualityRating')">{{ opportunity.qualityScore }}</el-descriptions-item>
      </el-descriptions>

    </el-card>
    <el-card class="common-card detail-header">
      <div class="section-title">{{t('competitors')}}</div>
      <div>
        <editor v-model="opportunity.competitors" :height="200" read-only />
      </div>
    </el-card>
    <el-card class="common-card detail-header">
      <div class="section-title-follow">{{t('Follow-upRecords')}}</div>
      <div v-for="(item, index) in followUps" :key="index" class="follow-record">
        <div class="left-border">
          <div class="follow-title">{{ getFollowTypeLabel(item.followType) }}</div>
          <div class="follow-desc">{{ item.content }}</div>
          <el-tag type="success" size="small">{{ getResultsLabel(item.result) }}</el-tag>
        </div>
        <div class="follow-time">{{t('Follow-upTime:')}}{{ item.followAt }}</div>
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
import {useI18n} from "vue-i18n";
const {proxy} = getCurrentInstance()!;
const {t} = useI18n();
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
