package cn.chenhaonee.parallel.core;

import cn.chenhaonee.parallel.common.Entry;
import cn.chenhaonee.parallel.core.concurrent.ConcurrentFactory;
import cn.chenhaonee.parallel.result.ResultUnit;
import cn.chenhaonee.parallel.result.SyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author nichenhao
 */
@Service
@Slf4j
public class ConcurrentService {

    @Autowired
    private ConcurrentFactory concurrentFactory;

    public <T, F> SyncResult<T, F> submit(List<T> bizIds, Function<T, F> function, String id, long timeout, TimeUnit unit, boolean allowPartSuccess) {
        ThreadPoolExecutor executor = concurrentFactory.findExecutor(id);

        Map<T, CompletableFuture<F>> map = bizIds.stream()
                .map(bizId -> supplyAsync(bizId, function, executor))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        CompletableFuture[] futures = map.values().toArray(new CompletableFuture[0]);

        CompletableFuture<Void> all = CompletableFuture.allOf(futures);
        try {
            all.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            String exName = e.getClass().getSimpleName();
            log.error("Tasks meet exception {}.", exName, e);

            if (!allowPartSuccess) {
                return SyncResult.failure(bizIds);
            }
        }

        Map<T, ResultUnit<F>> data = map.entrySet().stream()
                .map(entry -> {
                    T bizId = entry.getKey();
                    CompletableFuture<F> future = entry.getValue();
                    F f = future.getNow(null);
                    return new Entry<>(bizId, new ResultUnit<>(Objects.nonNull(f), f));
                })
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        return new SyncResult<>(data);
    }

    public <T> SyncResult<T, Void> execute(List<T> bizIds, Consumer<T> function, String id, long timeout, TimeUnit unit, boolean allowPartSuccess) {
        ThreadPoolExecutor executor = concurrentFactory.findExecutor(id);

        Map<T, CompletableFuture<Void>> map = bizIds.stream()
                .map(bizId -> runAsync(bizId, function, executor))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        CompletableFuture[] futures = map.values().toArray(new CompletableFuture[0]);

        CompletableFuture<Void> all = CompletableFuture.allOf(futures);
        try {
            all.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            String exName = e.getClass().getSimpleName();
            log.error("Tasks meet exception {}.", exName, e);

            if (!allowPartSuccess) {
                return SyncResult.failure(bizIds);
            }
        }

        Map<T, ResultUnit<Void>> data = map.entrySet().stream()
                .map(entry -> {
                    T bizId = entry.getKey();
                    CompletableFuture<Void> future = entry.getValue();
                    boolean isDone = future.isDone();
                    return new Entry<>(bizId, new ResultUnit<Void>(isDone, null));
                })
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        return new SyncResult<>(data);
    }

    private <T, F> Entry<T, CompletableFuture<F>> supplyAsync(T bizId, Function<T, F> function, ThreadPoolExecutor executor) {
        CompletableFuture<F> future = CompletableFuture.supplyAsync(() -> function.apply(bizId), executor);
        return new Entry<>(bizId, future);
    }

    private <T> Entry<T, CompletableFuture<Void>> runAsync(T bizId, Consumer<T> consumer, ThreadPoolExecutor executor) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> consumer.accept(bizId), executor);
        return new Entry<>(bizId, future);
    }
}
