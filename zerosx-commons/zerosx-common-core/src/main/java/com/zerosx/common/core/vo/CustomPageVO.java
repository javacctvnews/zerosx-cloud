package com.zerosx.common.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 *
 * @param <T>
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页对象")
public class CustomPageVO<T> implements Serializable {

    private static final long serialVersionUID = -7237100044972110056L;
    /**
     * 数据为空
     */
    public static final CustomPageVO EMPTY_PAGE = new CustomPageVO(0, null);

    //@Schema(description = "当前页")
    //protected long current = 1;

    @Getter
    @Schema(description = "总数")
    protected long total;

    @Schema(description = "当前分页记录集合")
    private List<T> list;

    /**
     * 处理导出时total为0
     * @return
     */
    public long getTotal() {
        if (total == 0 && CollectionUtils.isNotEmpty(list)) {
            return list.size();
        }
        return total;
    }
}
