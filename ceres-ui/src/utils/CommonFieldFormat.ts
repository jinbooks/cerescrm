export function formatRegion(province?: string, city?: string): string {
    if (province && city) {
        return `${province}/${city}`;
    } else if (province) {
        return province;
    } else if (city) {
        return city;
    }
    return ""; // 空字符串
}