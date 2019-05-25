package cn.chenhaonee.parallel.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author nichenhao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcurrentConfig {

    /**
     * 线程池id，全局唯一
     */
    @Getter
    private String id;
    /**
     * 核心线程数
     */
    @Getter
    private int corePoolSize;
    /**
     * 最大线程数
     */
    @Getter
    private int maximumPoolSize;
    /**
     * 连接时间，默认为5000
     */
    @Getter
    private long keepAliveTime = 5000;
    /**
     * 时间单位，默认为毫秒
     */
    @Getter
    private TimeUnit unit = TimeUnit.MILLISECONDS;

    /**
     * 队列大小
     */
    @Getter
    private int queueSize;
}
