package com.minhnv.dagger2androidpro.utils.rx;

import io.reactivex.Scheduler;

public interface ScheduleProvider {
    Scheduler computation();

    Scheduler io();

    Scheduler ui();
}
