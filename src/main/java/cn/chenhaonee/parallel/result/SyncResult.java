package cn.chenhaonee.parallel.result;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @param <F>
 * @author nichenhao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncResult<T, F> {

    private Map<T, ResultUnit<F>> data = new HashMap<>();

    public static <M, N> SyncResult<M, N> failure(List<M> bizIds) {
        SyncResult<M, N> result = new SyncResult<>();
        bizIds.forEach(bizId -> {
            result.data.putIfAbsent(bizId, ResultUnit.failure());
        });
        return result;
    }

    public List<ResultUnit<F>> results() {
        return Lists.newArrayList(data.values());
    }

}
