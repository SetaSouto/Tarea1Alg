import os

import pandas as pd

buffers = [1, 1000, 10000, 100000, 1000000, 15000000]

for buf in buffers:
    path = "Csv/buf=" + str(buf) + "/"
    csvs = []
    for filePath in os.listdir(path):
        csvs.append(pd.read_csv(path + filePath))
    csv = pd.concat(csvs)
    csv.to_csv("bigCSV.csv");
