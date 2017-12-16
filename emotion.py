import glob
import random
import math
import numpy
from numpy import * 
import itertools
import pandas as pd
from sklearn.svm import SVC
import numpy.core._methods
import numpy.lib.format
emotions=["neutral","happy","sad","surprise","disgust"] #emotions list
clf = SVC(kernel='linear', probability=True, tol=1e-3)#, verbose = True) #Set the classifier as a support vector machines with polynomial kernel
data = {} #Make dictionary for all values

def get_train_predict_data():
    feature_data=open("F:\\emotion final yr\\test.txt","r")
    lines=feature_data.readlines();
    lines=[line.strip().split(',') for line in lines]
    random.shuffle(lines)
    lines=numpy.array(lines)
    training = lines[:int(len(lines)*0.8),0:24] #get first 80% of file list
    prediction = lines[-int(len(lines)*0.2):,0:24] #get last 20% of file list
    training_labels=lines[:int(len(lines)*0.8),24:]
    prediction_labels=lines[-int(len(lines)*0.2):,24:]
    return training, prediction, training_labels, prediction_labels
def get_vectorised_data(data,i):
    xlist=[]
    ylist=[]
    for j in range(0,23,2):
        xlist.append(data[i][j])
        ylist.append(data[i][j+1])
    xlist1=numpy.array(xlist).astype(numpy.float)
    ylist1=numpy.array(ylist).astype(numpy.float)
    xmean = numpy.mean(xlist1)
    ymean = numpy.mean(ylist1)
    xcentral = [(x-xmean) for x in xlist1]
    ycentral = [(y-ymean) for y in ylist1]

    landmarks_vectorised = []
    for x, y, w, z in zip(xcentral, ycentral, xlist1, ylist1):
        landmarks_vectorised.append(w)
        landmarks_vectorised.append(z)
        meannp = numpy.asarray((ymean,xmean))
        coornp = numpy.asarray((z,w))
        dist = numpy.linalg.norm(coornp-meannp)
        landmarks_vectorised.append(dist)
        landmarks_vectorised.append((math.atan2(y, x)*360)/(2*math.pi))
    return landmarks_vectorised
def make_sets():
    train_data=[]
    predict_data=[]
    training_data = []
    training_labels = []
    prediction_data = []
    prediction_labels = []
    training,prediction,training_labels,prediction_labels=get_train_predict_data()
    len_train=len(training)
    for i in range(0,len_train):
        train_data=get_vectorised_data(training,i)
        training_data.append(train_data)
    len_predict=len(prediction)
    for i in range(0,len_predict):
        predict_data=get_vectorised_data(prediction,i)
        prediction_data.append(predict_data)
    return training_data, training_labels, prediction_data, prediction_labels 

accur_lin = []
for i in range(0,10):
    print("Making sets %s" %i) #Make sets by random sampling 80/20%
    training_data, training_labels, prediction_data, prediction_labels = make_sets()

    npar_train = numpy.array(training_data) #Turn the training set into a numpy array for the classifier
    npar_trainlabs = numpy.array(training_labels)
    print("training SVM linear %s" %i) #train SVM
    clf.fit(npar_train, training_labels)

    print("getting accuracies %s" %i) #Use score() function to get accuracy
    npar_pred = numpy.array(prediction_data)
    pred_lin = clf.score(npar_pred, prediction_labels)
    print "linear: ", pred_lin
    accur_lin.append(pred_lin) #Store accuracy in a list
print accur_lin
print("Mean value lin svm: %s" %numpy.mean(accur_lin)) #FGet mean accuracy of the 10 runs
