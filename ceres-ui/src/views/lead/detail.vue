<template>
  <div class="app-container" v-loading="submitLoading">
    <el-card class="common-card detail-header">
      <el-divider><span>线索基本信息</span></el-divider>
      <el-descriptions :column="4" border label-class-name="label-style" style="margin-top: 15px">
        <el-descriptions-item :label="t('clueCoding')">{{ lead.leadCode }}</el-descriptions-item>
        <el-descriptions-item :label="t('ContactName')">{{ lead.name }}</el-descriptions-item>
        <el-descriptions-item :label="t('jbx.organizations.typeCompany')">{{ lead.company }}</el-descriptions-item>
        <el-descriptions-item :label="t('jbx.users.jobTitle')">{{ lead.title }}</el-descriptions-item>

        <el-descriptions-item :label="t('appLoginTextMobile')">{{ lead.phone }}</el-descriptions-item>
        <el-descriptions-item :label="t('jbx.institutions.email')">{{ lead.email }}</el-descriptions-item>
        <el-descriptions-item :label="t('wechat')">{{ lead.wechat}}</el-descriptions-item>
        <el-descriptions-item :label="t('Region')">{{ lead.province }}{{lead.city}}</el-descriptions-item>

        <el-descriptions-item :label="t('SourceType')">{{ getSourceTypeLabel(lead.sourceType) }}</el-descriptions-item>
        <el-descriptions-item :label="t('SourceDetails')">
          <el-tooltip class="item" effect="dark" :content="lead.sourceDetail" placement="top">
            <span class="ellipsis">{{ lead.sourceDetail}}</span>
          </el-tooltip>
        </el-descriptions-item>
        <el-descriptions-item :label="t('industry')">{{ getIndustryLabel(lead.industry) }}</el-descriptions-item>
        <el-descriptions-item :label="t('budget')">{{ formatAmount(lead.budget) }}</el-descriptions-item>


        <el-descriptions-item :label="t('org.status')">{{ getStatusLabel(lead.status) }}</el-descriptions-item>
        <el-descriptions-item :label="t('QualityRating')">
          <el-rate v-model="lead.qualityScore" disabled/>
        </el-descriptions-item>
        <el-descriptions-item :label="t('priority')">{{ getPriorityLabel(lead.priority) }}</el-descriptions-item>
        <el-descriptions-item :label="t('PersonCharge')">{{ lead.ownerName || "无"}}</el-descriptions-item>

        <el-descriptions-item :label="t('AllocateTime')">{{ lead.assignedAt }}</el-descriptions-item>
        <el-descriptions-item :label="t('NextFollowTime')">{{ lead.nextFollowTime }}</el-descriptions-item>
        <el-descriptions-item :label="t('LastContactTime')">{{ lead.lastContactTime }}</el-descriptions-item>
        <el-descriptions-item :label="t('ConversionTime')">{{ lead.convertedAt }}</el-descriptions-item>

      </el-descriptions>
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
import {getOne} from "@/api/lead/lead";
import {createValueLabelMap, getLabelFromMap} from "@/utils/Dict-utils";
import {formatAmount} from "@/utils";
import {useI18n} from "vue-i18n";
const {proxy} = getCurrentInstance()!;
const {source_type, books_industry, status_manage, priority, follow_types, results}
    = proxy?.useDict("source_type", "books_industry", "status_manage", "priority", "follow_types", "results");
const router = useRouter()
const submitLoading = ref(true)

const {t} = useI18n();

//行业
const flatIndustryList = books_industry.value.flatMap(group => group.options)
const industryMap = createValueLabelMap(flatIndustryList)
const getIndustryLabel = (val: number) => getLabelFromMap(industryMap, val)
//状态
const statusMap = createValueLabelMap(status_manage.value)
const getStatusLabel = (val: number) => getLabelFromMap(statusMap, val)
//来源类型
const sourceTypeMap = createValueLabelMap(source_type.value)
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

// 获取路由参数
const leadId = router.currentRoute.value.query.id as string

// 客户基础信息
const lead = ref<any>({})
// 跟进信息
const followUps = ref<any>([])

function loadDetails() {
  if (!leadId) return
  submitLoading.value = true;
  getOne(leadId).then((res: any) => {
    lead.value = res.data.lead
    followUps.value = res.data.followUp
    submitLoading.value = false
  })
}


onMounted(() => {
  loadDetails()
})
</script>

<style scoped>
/* 加在 style scoped 中 */
.ellipsis {
  display: inline-block;
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: bottom;
}

.section-title {
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
