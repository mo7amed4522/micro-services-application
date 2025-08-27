import md5 from 'js-md5';
import { ElMessage } from 'element-plus';

/**
 * 处理时间格式
 * @param {*} isoString
 * @returns
 */
export const _timeFormat = (isoString) => {
  // 使用正则表达式去掉时区信息，并获取日期和时间部分
  const match = isoString.match(/^(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})/);

  if (match) {
    // 替换 'T' 为 ' ' 以便格式化输出
    return match[1].replace('T', ' ');
  }

  return null; // 如果匹配失败，返回 null
};

// 生成sign
export const generateSign = (formData, signKey) => {
  // 过滤掉 sign 字段，因为 sign 是最后生成的
  const filteredData = { ...formData };
  delete filteredData.sign;

  // 按照参数名的 ASCII 字典序排序
  const sortedKeys = Object.keys(filteredData).sort();

  // 拼接成 key=value&key=value 的格式
  const stringA = sortedKeys.map((key) => `${key}=${filteredData[key]}`).join('&');

  // 拼接 signKey
  const stringSignTemp = `${stringA}&signKey=${signKey}`;
  return md5(stringSignTemp);
  // 计算 MD5 并返回
  // return md5(stringSignTemp).toUpperCase() // 使用 js-md5 计算 MD5 加密
};

export const formatToTwoDecimals = (value) => {
  const num = parseFloat(value);
  return isNaN(num) ? '0.00' : num.toFixed(2);
};

// 你的 copyText 函数
export const copyText = async (text, t) => {
  // 检查页面是否在 HTTPS 环境下，或者是文件协议
  if (window.location.protocol !== 'https:' && window.location.protocol !== 'file:') {
    // 使用 execCommand 作为回退方案
    try {
      const textArea = document.createElement('textarea');
      textArea.value = text.replace(/\n/g, '\r\n'); // 替换换行符（针对不同系统的换行符）
      document.body.appendChild(textArea);
      textArea.select(); // 选中文本
      const success = document.execCommand('copy'); // 使用 execCommand 进行复制
      document.body.removeChild(textArea);

      if (success) {
        ElMessage.success(t('common.copySuccess')); // 调用 t 来获取国际化文本
      } else {
        console.error('execCommand Copy failed');
        ElMessage.error(t('common.copyFail'));
      }
    } catch (error) {
      console.error('execCommand Copy failed:', error);
      ElMessage.error(t('common.copyFail'));
    }
  } else {
    // 使用 Clipboard API 进行复制（要求 HTTPS 环境）
    try {
      await navigator.clipboard.writeText(text);
      ElMessage.success(t('common.copySuccess'));
    } catch (error) {
      console.error('Clipboard API Copy failed:', error);
      ElMessage.error(t('common.copyFail'));
    }
  }
};

// 返回时分秒
export const formatTime = (seconds) => {
  // 如果是负数，返回 0
  if (seconds < 0) return 0;

  // 计算小时、分钟、秒
  const hours = Math.floor(seconds / 3600); // 计算小时
  const minutes = Math.floor((seconds % 3600) / 60); // 计算分钟
  const secs = seconds % 60; // 计算秒数

  // 返回时分秒格式，确保每个部分都是两位数
  return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
};

export const maskMiddle8 = (input) => {
  if (typeof input !== 'string' || input.length !== 12) {
    console.warn('输入必须是12位字符串');
    return input;
  }

  const prefix = input.slice(0, 2);
  const suffix = input.slice(-2);
  const masked = '*'.repeat(8);

  return `${prefix}${masked}${suffix}`;
};
