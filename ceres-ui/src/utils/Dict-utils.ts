
/**
 * 创建一个 Map，从 value 映射到 label
 * @param list 形如 [{ label: '新线索', value: 1 }]
 */
export function createValueLabelMap(list: { label: string; value: any }[]) {
    return new Map(list.map(item => [item.value, item.label]))
}

/**
 * 从 Map 中获取 label，若无匹配则返回默认值
 */
export function getLabelFromMap(map: Map<any, string>, value: any, defaultLabel = '未知') {
    return map.get(value) || defaultLabel
}
