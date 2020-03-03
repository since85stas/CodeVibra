package ru.batura.stat.codevibra.ChessClockRx;

public interface ChessStateChageListner {
    public void timeChange(long time);

    void timeFinish();

    void nextInterval(boolean bol);
}
