package com.zhenhui.demo.sparklers.domain.interactor;

import java.util.concurrent.Executor;

import com.google.common.base.Preconditions;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase<T, R>{

    private final Executor threadExecutor;
    private final CompositeDisposable disposables;

    public UseCase(Executor executor) {
        this.threadExecutor = executor;
        this.disposables = new CompositeDisposable();
    }

    public void execute(T params, DisposableObserver<R> observer) {
        Preconditions.checkNotNull(observer);
        final Observable<R> observable = run(params)
            .subscribeOn(Schedulers.from(threadExecutor));
        addDisposable(observable.subscribeWith(observer));
    }

    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    private void addDisposable(Disposable disposable) {
        Preconditions.checkNotNull(disposable);
        Preconditions.checkNotNull(disposables);
        disposables.add(disposable);
    }

    protected abstract Observable<R> run(T params);
}
