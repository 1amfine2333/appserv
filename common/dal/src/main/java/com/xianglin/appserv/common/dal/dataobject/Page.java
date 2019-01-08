package com.xianglin.appserv.common.dal.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**默认page对象
 * Created by wanglei on 2017/9/23.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    @Builder.Default
    private int startPage = 1;

    @Builder.Default
    private int pageSize = 10;

    /**取起始位置值
     * @return
     */
    public int getOffset(){
        if(this.startPage < 1){
            this.startPage = 1;
        }
        return (this.startPage - 1)*pageSize;
    }
}
