# coding:utf-8
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium import webdriver
import time
import json
import os
import random

options = webdriver.ChromeOptions()
options.add_argument('user-agent="MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1"')
options.add_argument('--no-sandbox')#解决DevToolsActivePort文件不存在的报错
options.add_argument('--disable-gpu') #谷歌文档提到需要加上这个属性来规避bug
options.add_argument('--hide-scrollbars') #隐藏滚动条, 应对一些特殊页面
options.add_argument('--disable-dev-shm-usage')
options.add_argument('--headless')



browser = webdriver.Chrome(chrome_options=options)
url='https://office.chaoxing.com/front/third/apps/seat/index?fidEnc=ffe2f1abb4a9f335'
browser.set_window_size(460,900)




def get_login():
    browser.get('https://office.chaoxing.com')
    time.sleep(1)
    with open('cookies_3', 'r', encoding='utf8') as f:
        listCookies = json.loads(f.read())
    for cookie in listCookies:
        browser.add_cookie(cookie)
    browser.get(url)
    time.sleep(0.5)
    try:
        course()
    finally:
        
        browser.quit()




def course(): 
    for times in range(0,5):  
        url_room='https://office.chaoxing.com/front/third/apps/seat/index?fidEnc=ffe2f1abb4a9f335'
        browser.get(url_room)
        time.sleep(2)
        try:
            browser.find_element_by_xpath('/html/body/div/div[2]/div[2]/span[2]').click()
            time.sleep(2)
        except BaseException:
            pass
        finally:
            time.sleep(2)
            browser.find_element_by_xpath('/html/body/div/div/div[1]/dl/dd/ul/li[3]').click()
            time.sleep(2)
            browser.find_element_by_xpath('/html/body/div/div/div[3]/div[2]/span[2]').click()
            time.sleep(2)
        

if __name__ == '__main__':
    get_login()
