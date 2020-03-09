package ru.batura.stat.codevibra.ChessClockRx;

import android.util.Log;

import rx.Subscriber;

public class ChessClockSubscriberSound extends Subscriber<Long> {

    public ChessStateChageListner mListner;

    long[] timeIntervalsSub;

    long saved;

    int count = 0;
    
    public static final String TAG = ChessClockSubscriberSound.class.getName();


    public ChessClockSubscriberSound(ChessStateChageListner listner, long[] timeIntervals) {
        super();
        mListner = listner;

        timeIntervalsSub = new long[timeIntervals.length+1];
        timeIntervalsSub[0] = 0;
        timeIntervalsSub[1] = timeIntervals[0];
        for (int i = 2; i < timeIntervals.length+1; i++) {
            timeIntervalsSub[i] = timeIntervalsSub[i-1] + timeIntervals[i-1];
        }
        Log.d(TAG, "onstart: ");
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
        if (count<timeIntervalsSub.length -2 && aLong >= timeIntervalsSub[count] ) {
            int interval = count ;
            mListner.nextInterval( interval %2 == 1  );
            mListner.setBold(count);
            count++;

        }
    }
}
