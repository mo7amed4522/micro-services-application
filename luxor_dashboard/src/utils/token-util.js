/* eslint-disable */
/**
 * token操作封装
 */
import { TOKEN_CACHE_NAME } from '@/config/setting';
import CryptoJS from 'crypto-js';
import { getBeijingHour } from '@/utils/common.js';

/**
 * 获取缓存的token
 */
export function getToken() {
  const token = localStorage.getItem(TOKEN_CACHE_NAME);
  if (!token) {
    return sessionStorage.getItem(TOKEN_CACHE_NAME);
  }
  return token;
}

/**
 * 缓存token
 * @param token token
 * @param remember 是否永久存储
 */
export function setToken(token, remember) {
  removeToken();
  if (token) {
    if (remember) {
      localStorage.setItem(TOKEN_CACHE_NAME, token);
    }
  }
}

/**
 * 移除token
 */
export function removeToken() {
  localStorage.removeItem(TOKEN_CACHE_NAME);
}

/**
 * 生成32位随机字符串
 */
function getRandom32() {
  // 生成32字节的十六进制字符串
  const array = new Uint8Array(16);
  let arr = window.crypto.getRandomValues(array);
  return Array.from(arr)
    .map((b) => b.toString(16).padStart(2, '0'))
    .join('');
}

export function tokenGenerate(timestamp, data = '') {
  if (data === '') {
    data = getRandom32();
  }
  const DECRYPT_SECRET = [
    'dJoJ!eB%Kh$',
    '!bx61wZ4^re',
    'mb@Xkr&Dc9n',
    '^3!Q+AF!ZZL',
    '%ofRPspj9#b',
    'qdr8t$X(51X',
    'JX$qwco$WjL',
    'BZPW5!8R_ix',
    'riY)a&LDk%f',
    'ZSH@4Wg6b#N',
    'QPyXOieE_)x',
    'e#&+w0aB$Eo',
    'R!i9wJvaoG#',
    'u2cFWZ_XDQ@',
    '%ar5Bz@^CmI'
  ];

  if (typeof timestamp !== 'string' || timestamp.length !== 13) {
    throw new Error('Invalid timestamp');
  }

  // Extract parts of the timestamp
  const timestampInt = parseInt(timestamp.substring(0, 10), 10);
  const lastThree = timestamp.slice(-3);
  const hourIndex = getBeijingHour() % 12;
  const second = timestampInt.toString().slice(-2);
  const key = DECRYPT_SECRET[hourIndex] + second + lastThree;
  // console.log(hourIndex, '-----hour');
  // console.log(second, '-----second');
  // console.log(key, '-----key');

  // Encrypt the data
  const encrypted = CryptoJS.AES.encrypt(data, CryptoJS.enc.Utf8.parse(key), {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });

  // Convert to base64
  return encrypted.ciphertext.toString(CryptoJS.enc.Base64);
}
