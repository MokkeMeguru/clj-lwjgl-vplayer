#!/usr/bin/env python
# coding:utf-8

import sys

if __name__ == '__main__':
    for line in sys.stdin:
        heads_num, knights_num = map(int, line.split())
        if heads_num == knights_num == 0: exit()
        heads = [int(input())for k in range(heads_num)]
        knights = [int(input())for k in range(knights_num)]
        heads.sort()
        knights.sort()
        sum = 0
        knights_pos = 0
        heads_pos = 0
        while (True):
            if (heads_pos == heads_num):
                print (sum)
                break
            if (knights_pos == knights_num):
                print ('Loowater is doomed')
                break
            if (knights [knights_pos] > heads [heads_pos]):
                knights_pos += 1
                heads_pos += 1
                sum = knights [knights_pos]
            else:
                knights_pos += 1
