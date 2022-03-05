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
day=str(time.strftime("%Y-%m-%d", time.localtime()))





def load_userdata():
    browser.get(url)
    time.sleep(5)
    browser.find_element_by_class_name('ipt-tel').clear()
    browser.find_element_by_class_name("ipt-pwd").clear()
    browser.find_element_by_class_name('ipt-tel').send_keys(tel)
    browser.find_element_by_class_name("ipt-pwd").send_keys(psw)
    time.sleep(1)
    browser.find_element_by_xpath('/html/body/div/div[2]/div/form/div[3]/button').click()
    time.sleep(1)
    result1=EC.visibility_of_element_located((By.CLASS_NAME, 'sind_top_word'))
    if result1:
        get_cookies()


def login_crouse():
    time.sleep(0.3)
    room='/html/body/div/ul[1]/li['+str(room_num)+']/span'
    sit='/html/body/div/div[2]/ul/li['+str(sit_num)+']'
    browser.find_element_by_xpath(room).click()
    time.sleep(0.2)
    result=EC.visibility_of_element_located((By.CLASS_NAME, 'time_sure'))
    if result:
        chart=browser.find_elements_by_class_name('time_cell')
        try:
            chart[time_start].click()
            chart[time_end].click()
            browser.find_element_by_class_name('time_sure').click()
            browser.find_element_by_xpath(sit).click()
            browser.find_element_by_class_name('order_bottom').click()        
        except BaseException:
            pass


def get_login():
    browser.get('https://office.chaoxing.com')
    if os.path.exists('cookies'):
        pass
    else:
        load_userdata()
    with open('cookies', 'r', encoding='utf8') as f:
        listCookies = json.loads(f.read())
    for cookie in listCookies:
        browser.add_cookie(cookie)
    browser.get(url)
    
    result1=EC.visibility_of_element_located((By.CLASS_NAME, 'lg-container'))
    if result1:
        get_cookies()
    else:
        get_login()

def course(): 
    while True:
        try:
            url_room='https://office.chaoxing.com/front/third/apps/seat/list?deptIdEnc=ffe2f1abb4a9f335'
            browser.get(url_room)
            time.sleep(0.3)
            try:
                result1= browser.find_elements_by_class_name('scr_list')
                print('17.30-21.30')
            finally: 
                if len(result1)==0:
                    print(time.strftime("%H:%M:%S", time.localtime()))
                else:
                    for i in range(0,5):
                        url_room='https://office.chaoxing.com/front/third/apps/seat/list?deptIdEnc=ffe2f1abb4a9f335'
                        browser.get(url_room)
                        time.sleep(0.2)
                        login_crouse()
            url_end='https://office.chaoxing.com/front/third/apps/seat/index?fidEnc=ffe2f1abb4a9f335'
            browser.get(url_end)
        except BaseException:
            pass
    
    
def get_cookies():
    dictCookies=browser.get_cookies()
    jsonCookies = json.dumps(dictCookies)
    with open(cookies, 'w') as f:
        f.write(jsonCookies)
        f.close()
    course()
    
    
def main(i):
    global n,tel,psw,room_num,sit_num,time_start,time_end
    n=i[6]
    tel=i[0]
    psw=i[1]
    room_num=i[2]
    sit_num=i[3]
    time_start=int(i[4])
    time_end=int(i[5])
    print(n,tel,psw,room_num,sit_num,time_start,time_end)
    global cookies
    cookies='cookies_'+str(n)
    get_login()
