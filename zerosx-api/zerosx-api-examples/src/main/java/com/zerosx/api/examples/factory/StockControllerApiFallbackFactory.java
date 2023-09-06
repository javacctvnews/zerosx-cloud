package com.zerosx.api.examples.factory;

import com.zerosx.api.examples.IStockControllerApi;
import com.zerosx.api.examples.dto.CommodityDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockControllerApiFallbackFactory implements FallbackFactory<IStockControllerApi> {

    @Override
    public IStockControllerApi create(Throwable cause) {
        return new IStockControllerApi() {
            @Override
            public ResultVO<?> decreaseStock(CommodityDTO commodityDTO) {
                return ResultVOUtil.feignFail("库存扣减失败");
            }
        };
    }
}
