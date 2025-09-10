import i18n from "@/languages";

const errorCode: { [key: string]: string } = {
  '401': i18n.global.t('errorCode401'),
  '403': i18n.global.t('errorCode403'),
  '404': i18n.global.t('errorCode404'),
  'default': i18n.global.t('errorCodeDefault'),
};

export default errorCode
