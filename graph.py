import os

import pandas as pd
import matplotlib.pyplot as plt

# buffers = [1, 1000, 100000, 1000000, 15000000]
buffers = [1]
significantDigits = 3

def createFigure(fig, df1, df2, feature, i):
    sub = fig.add_subplot(2,2,i)
    sub.set_xlabel('N [10^' + str(significantDigits) + ']')
    sub.set_ylabel(feature)
    sub.scatter(df1['N'] / 10**significantDigits, df1[feature], c = 'r', s = 20)
    sub.scatter(df2['N']  / 10**significantDigits, df2[feature], c = 'g', s = 20)
    sub.grid()

def plot(df):
    df1 = csv.loc[csv['Heuristic'] == 'Linear Split']
    df2 = csv.loc[csv['Heuristic'] == 'Greene Split']

    fig = plt.figure()
    titile = fig.suptitle("Split heuristic comparisson for bufferSize = " + str(buf))

    createFigure(fig, df1, df2, 'CreationTime', 1)
    createFigure(fig, df1, df2, 'UsagePercentage', 2)
    createFigure(fig, df1, df2, 'QueriesTime', 3)
    createFigure(fig, df1, df2, 'DiscAccess', 4)

    plt.show()

for buf in buffers:
    print("Buffer size = " + str(buf))
    path = "Csv/buf=" + str(buf) + "/"
    csvs = []
    for filePath in os.listdir(path):
        csvs.append(pd.read_csv(path + filePath))
    csv = pd.concat(csvs)

    plot(csv)
