package cn.chenhaonee.parallel;

import cn.chenhaonee.parallel.core.ConcurrentService;
import cn.chenhaonee.parallel.result.SyncResult;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class Parallel<T> {

    private List<T> bizIds;

    private long timeout = 5000;

    private TimeUnit unit = TimeUnit.MILLISECONDS;

    private boolean allowPartSuccess = false;

    private ConcurrentService concurrentService;

    private String executorId = "DEFAULT";

    public static <T> Parallel<T> of(List<T> bizKey) {
        Parallel<T> parallel = new Parallel<>();
        parallel.bizIds = bizKey;
        return parallel;
    }

    public Parallel<T> timeout(long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
        return this;
    }

    public Parallel<T> parallel(String executorId) {
        this.executorId = executorId;
        return this;
    }

    public Parallel<T> allowPartSuccess(boolean allow) {
        this.allowPartSuccess = allow;
        return this;
    }

    public Parallel<T> executor(ConcurrentService concurrentService) {
        this.concurrentService = concurrentService;
        return this;
    }


    public <R> SyncResult<T, R> submit(Function<T, R> function) {
        if (concurrentService == null) {
            throw new RuntimeException("You need configure ConcurrentService first");
        }
        return concurrentService.submit(bizIds, function, executorId, timeout, unit, allowPartSuccess);
    }

    public SyncResult<T, Void> execute(Consumer<T> consumer) {
        if (concurrentService == null) {
            throw new RuntimeException("You need configure ConcurrentService first");
        }
        return concurrentService.execute(bizIds, consumer, executorId, timeout, unit, allowPartSuccess);
    }

}
