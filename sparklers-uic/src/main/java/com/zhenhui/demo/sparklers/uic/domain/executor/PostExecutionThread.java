package com.zhenhui.demo.sparklers.uic.domain.executor;

import io.reactivex.Scheduler;

public interface PostExecutionThread {

    Scheduler getScheduler();

}
