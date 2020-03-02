package ru.batura.stat.codevibra.ChessClockRx;

import android.util.Log;

import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder;

import rx.Subscriber;

public class ChessClockSubscriber extends Subscriber<Long> {

    public ChessStateChageListner mListner;

    long[] timeIntervalsSub;

    long saved;

    int count = 0;
    
    public static final String TAG = ChessClockSubscriber.class.getName();


    public ChessClockSubscriber(ChessStateChageListner listner, long[] timeIntervals) {
        super();
        mListner = listner;

        timeIntervalsSub = new long[timeIntervals.length+1];
        timeIntervalsSub[0] = 0;
        timeIntervalsSub[1] = timeIntervals[0];
        for (int i = 2; i < timeIntervals.length-1; i++) {
            timeIntervalsSub[i] = timeIntervalsSub[i-1] + timeIntervals[i];
        }
    }

    @Override
    public void onCompleted() {
        stopTimer();
        Log.d(TAG, "onCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "onError: ");
    }

    private void stopTimer() {
        mListner.timeFinish();
        unsubscribe();
    }

    @Override
    public void onNext(Long aLong) {
        saved = aLong;
        mListner.timeChange(aLong);
        if (count<timeIntervalsSub.length && aLong >= timeIntervalsSub[count] ) {
            count++;
            mListner.nextInterval();
        }
    }
}
