package cn.chenhaonee.parallel.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @param <T>
 * @param <F>
 * @author nichenhao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entry<T, F> {

    private T key;

    private F value;
}
