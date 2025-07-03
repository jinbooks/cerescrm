/** 通用的 label/value 项 */
export interface LabeledValue {
    label: string;
    value: string | number;
}

/** 分组选项（如 el-option-group） */
export interface GroupedOptions {
    label: string;
    options: LabeledValue[];
}
