package com.smartapp.hztech.smarttebletapp.listeners;

public class AsyncResultBag {
    public interface Error {
        void onError(Object error);
    }

    public interface Before {
        void beforeExecuting();
    }

    public interface Success {
        void onSuccess(Object result);
    }
}
