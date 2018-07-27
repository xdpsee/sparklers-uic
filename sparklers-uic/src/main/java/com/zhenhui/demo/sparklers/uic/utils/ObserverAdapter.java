package com.zhenhui.demo.sparklers.uic.utils;

import io.reactivex.observers.DisposableObserver;

public abstract class ObserverAdapter<T> extends DisposableObserver<T> {

    @Override
    public final void onNext(T data) {
        onSuccess(data);
    }

    @Override
    public final void onComplete() {

    }

    protected abstract void onSuccess(T data);
}
