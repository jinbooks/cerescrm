import {defineStore} from "pinia";
import {listStore} from "@/api/workspace/workspace";
import {setSetList} from "@/utils/Auth";

const workspaceStore: any = defineStore(
    'workspace',
    {
        state: () => ({
            initializeTask: false,
            workspaceId: "",
            termCurrent: undefined,
            termStart: undefined,
            setList: [],
            // 科目编码规则[4,2,2,2]
            subjectCodeLen: [],
            // 初始化状态
            initializeStatus: false,
        }),
        actions: {
            updateWorkspaceId(id: any) {
                this.workspaceId = id;
            },
            refreshData() {
                return listStore().then((res: any) => {
                    this.setList = res.data;
                    setSetList(this.setList);
                    if (!this.workspaceId && this.setList.length > 0) {
                        const item: any = this.setList[0];
                        this.workspaceId = item.workspaceId;
                    }
                    return res.data;
                });
            }
        }
    }
)

export default workspaceStore
