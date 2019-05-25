package cn.chenhaonee.parallel.core.concurrent;

import lombok.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author nichenhao
 */
@Data
public class ConcurrentTask<T,R> {

    private List<T> bizIds;

    private Function<T,R> function;

    private long timeout;

    private TimeUnit unit;

    private boolean allowPartSuccess;
}
