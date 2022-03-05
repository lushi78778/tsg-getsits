# coding:utf-8
from multiprocessing import Pool
import main


if __name__ == "__main__":
    f = open('data.csv', "r")
    lines = f.read().split("\n")
    pool = Pool(len(lines)-2)
    for line in lines:
        if line != "":
            i = line.split(",")
            if i[6]=='0':
                pass
            else:
                print(i)
                pool.map_async(main.main,[i])
    pool.close()#关闭进程池，不再接受新的进程
    pool.join()#主进程阻塞等待子进程的退出
