package com.sprice.hivemind.dht.node.event;

import com.sprice.hivemind.dht.event.Event;
import com.sprice.hivemind.dht.event.EventProtocol;

import java.util.concurrent.Future;

public class SubmitTaskEvent extends Event {

    private Runnable task;
    private Future future;

    public SubmitTaskEvent(Runnable task, Future future) {
        this.task = task;
        this.future = future;
    }

    public Runnable getTask() {
        return task;
    }

    public Future getFuture() {
        return future;
    }

    @Override
    public int getType() {
        return EventProtocol.SUBMIT_TASK_EVENT;
    }

}
