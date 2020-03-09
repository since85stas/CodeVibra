package ru.batura.stat.codevibra.ChessClockRx;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import ru.batura.stat.codevibra.ui.main.MainViewModel;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class ChessClockRx {

    public static final String TAG = "ChessClockRx";

    long[] timeIntervals;

    long interval;

    MainViewModel model;

    long timeFrom = 0;

    boolean isRunning = false;

    Subscription mSubscription;

    public ChessClockRx(MainViewModel model, long[] array, long interval  ) {
        this.model = model;
        timeIntervals = array;
        this.interval = interval;
        rxChessTimer();

    }

    public void rxChessTimer() {
        if (!isRunning) {
            isRunning = true;
            mSubscription = initChessClockObserver().
                    subscribeOn(Schedulers.io()).
                    onBackpressureBuffer().
                    subscribe(new ChessClockSubscriberBold(model,interval));
        } else {
            isRunning = false;
            mSubscription.unsubscribe();
        }
    }


    private Observable<Long> initChessClockObserver() {

        long fullTime = 0;
        for (long t: timeIntervals
        ){
            fullTime += t;
        }

        final long end = fullTime;

        Observable<Long> obs = Observable.interval(10, TimeUnit.MILLISECONDS).
                map(i -> i*10).
                takeUntil(aLong -> (aLong > end - 15));

        return obs;
    }

    public void stopTimer() {
        logOnCompleted();
        mSubscription.unsubscribe();
    }

    private void logOnCompleted() {
        Log.d(TAG, "The day has come, may my watch end!");
    }

}
