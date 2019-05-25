package cn.chenhaonee.parallel.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @param <T>
 * @author nichenhao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultUnit<T> {

    private boolean success;

    private T result;

    public static <M> ResultUnit<M> failure() {
        ResultUnit<M> result = new ResultUnit<>();
        result.success = false;
        return result;
    }

}
