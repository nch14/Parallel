package cn.chenhaonee.parallel.core.concurrent;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author nichenhao
 */
public interface ConcurrentFactory {

    ThreadPoolExecutor findExecutor(String id);
}
