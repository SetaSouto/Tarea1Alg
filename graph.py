import os

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

buffers = [1, 1000, 10000, 100000, 1000000, 15000000]
#buffers = [1]
significantDigits = 3

# Experiments for buffersize = 1
M = 4096
m = 4 * M / 10

def denominatorQueries(n):
    return M * (np.log(n) / np.log(M))

def denominatorQueries2(n):
    return M ** (np.log(n) / np.log(M))

def denominatorCreation(n):
    return M * (np.log(n**2) / np.log(M)) + m**(np.log(n) / np.log(M) - 1) * M
    #return n * M * (np.log(n) / np.log(M)) + n*M

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

def plot2(n, metric, denominator, name, label):
    fig = plt.figure()
    fig.suptitle(name)

    sub1 = fig.add_subplot(1, 2, 1)
    sub1.set_xlabel('N [10^' + str(significantDigits) + ']')
    sub1.set_ylabel(label)
    sub1.set_title("Normal")
    sub1.scatter(n / 10**significantDigits, metric, c = 'r', s = 20)
    sub1.grid()

    sub2 = fig.add_subplot(1, 2, 2)
    sub2.set_xlabel('N [10^' + str(significantDigits) + ']')
    sub2.set_ylabel(label)
    sub2.set_title("Normalized")
    sub2.scatter(n / 10**significantDigits, metric / denominator(n), c = 'r', s = 20)
    sub2.grid()

    #plt.axis([0.0, 20.0, 0.0, max(n)])
    plt.show()

# Plot all metrics for all buffer sizes
for buf in buffers:
    print("Buffer size = " + str(buf))
    path = "Csv/buf=" + str(buf) + "/"
    csvs = []
    for filePath in os.listdir(path):
        csvs.append(pd.read_csv(path + filePath))
    csv = pd.concat(csvs)

    plot(csv)

    # Detailed look at the case with bufferSize = 1
    if (buf == 1):
        df1 = csv.loc[csv['Heuristic'] == 'Linear Split']
        df2 = csv.loc[csv['Heuristic'] == 'Greene Split']

        plot2(df1['N'], df1['DiscAccess'] / df1['N'], denominatorQueries, "Linear Split", 'DiscAccess')
        plot2(df2['N'], df2['DiscAccess'] / df2['N'], denominatorQueries, "Greene Split", 'DiscAccess')

        plot2(df1['N'], df1['DiscAccess'] / df1['N'], denominatorQueries2, "Linear Split", 'DiscAccess')
        plot2(df2['N'], df2['DiscAccess'] / df2['N'], denominatorQueries2, "Greene Split", 'DiscAccess')

        plot2(df1['N'], df1['CreationTime'], denominatorCreation, "Linear Split", 'CreationTime')
        plot2(df2['N'], df2['CreationTime'], denominatorCreation, "Greene Split", 'CreationTime')
