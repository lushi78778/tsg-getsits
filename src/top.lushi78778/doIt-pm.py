# !/usr/bin/env python3
# _*_ coding:utf-8 _*_
"""
@File     : doIt-pm.py
@Project  : useful
@Time     : 2021/10/25 10:21
@Author   : lushi
@Contact  : lushi78778@outlook.com
@Software : PyCharm
@Last Modify Time      @Version     @Desciption
------------------     --------     -----------
2021/10/25 10:21        1.0             None
"""

from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium import webdriver
import time
import json
import os

options = webdriver.ChromeOptions()
options.add_argument(
    'user-agent="MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1"')
options.add_argument('--no-sandbox')  # 解决DevToolsActivePort文件不存在的报错
options.add_argument('--disable-gpu')  # 谷歌文档提到需要加上这个属性来规避bug
options.add_argument('--hide-scrollbars')  # 隐藏滚动条, 应对一些特殊页面
options.add_argument('--ignore-certificate-errors')  # 主要是该条
options.add_argument('--ignore-ssl-errors')

options.add_argument('--disable-dev-shm-usage')
options.add_argument('--headless')

browser = webdriver.Chrome(options=options)

url = 'https://office.chaoxing.com/front/third/apps/seat/index?fidEnc=ffe2f1abb4a9f335'
browser.set_window_size(460, 900)
day = str(time.strftime("%Y-%m-%d", time.localtime()))

num_room = '5966'
num_sit = '/html/body/div/div[2]/ul/li[72]'


def load_userdata():
    browser.get(url)
    time.sleep(5)
    browser.find_element_by_class_name('ipt-tel').clear()
    browser.find_element_by_class_name("ipt-pwd").clear()
    browser.find_element_by_class_name('ipt-tel').send_keys('15100237971')
    browser.find_element_by_class_name("ipt-pwd").send_keys('122573zpr')
    time.sleep(0.3)
    browser.find_element_by_xpath('/html/body/div/div[2]/div/form/div[3]/button').click()
    time.sleep(0.3)
    result1 = EC.visibility_of_element_located((By.CLASS_NAME, 'sind_top_word'))
    if result1:
        get_cookies()


def login_crouse():
    time.sleep(0.1)
    browser.find_element_by_xpath('/html/body/div/ul[1]/li[5]/span').click()
    time.sleep(0.2)
    result = EC.visibility_of_element_located((By.CLASS_NAME, 'time_sure'))
    if result:
        chart = browser.find_elements_by_class_name('time_cell')
        try:
            chart[12].click()
            chart[19].click()
            browser.find_element_by_class_name('time_sure').click()
            browser.find_element_by_xpath(num_sit).click()
            browser.find_element_by_class_name('order_bottom').click()
            print('已尝试提交')
        except BaseException:
            print('出错')
            pass


def get_login():
    browser.get('https://office.chaoxing.com')
    if os.path.exists('cookies_1'):
        pass
    else:
        load_userdata()
    with open('cookies_1', 'r', encoding='utf8') as f:
        listCookies = json.loads(f.read())
    for cookie in listCookies:
        browser.add_cookie(cookie)
    browser.get(url)
    time.sleep(0.2)
    result1 = EC.visibility_of_element_located((By.CLASS_NAME, 'lg-container'))
    if result1:
        get_cookies()
    else:
        get_login()


def course():
    while True:
        try:
            url_room = 'https://office.chaoxing.com/front/third/apps/seat/index?fidEnc=ffe2f1abb4a9f335'
            browser.get(url_room)
            time.sleep(0.3)
            browser.find_element_by_xpath('/html/body/div/ul/li[1]/p').click()
            time.sleep(0.3)
            try:
                result1 = browser.find_elements_by_class_name('scr_list')
                print('检测scr_list')
            finally:
                if len(result1) == 0:
                    print('未到开始时间')
                else:
                    print('已到开始时间')
                    for i in range(0, 5):
                        url_room = 'https://office.chaoxing.com/front/third/apps/seat/list?deptIdEnc=ffe2f1abb4a9f335'
                        browser.get(url_room)
                        time.sleep(0.2)
                        login_crouse()
            url_end = 'https://office.chaoxing.com/front/third/apps/seat/index?fidEnc=ffe2f1abb4a9f335'
            browser.get(url_end)
        except BaseException:
            pass


def get_cookies():
    dictCookies = browser.get_cookies()
    jsonCookies = json.dumps(dictCookies)
    with open('cookies_1', 'w') as f:
        f.write(jsonCookies)
        f.close()
    course()


if __name__ == '__main__':
    get_login()
