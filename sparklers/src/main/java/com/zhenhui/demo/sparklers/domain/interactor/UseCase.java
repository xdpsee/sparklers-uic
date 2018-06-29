package com.zhenhui.demo.sparklers.domain.interactor;

import com.google.common.base.Preconditions;
import com.zhenhui.demo.sparklers.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.domain.executor.ThreadExecutor;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase<T, R> {

    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    UseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    abstract Observable<R> buildObservable(T params);

    public void execute(T params, Observer<R> observer) {
        Preconditions.checkNotNull(observer);
        final Observable<R> observable = this.buildObservable(params);

        if (threadExecutor != null) {
            observable.subscribeOn(Schedulers.from(threadExecutor));
        }

        if (postExecutionThread != null) {
            observable.observeOn(postExecutionThread.getScheduler());
        }

        observable.subscribe(observer);
    }
}

