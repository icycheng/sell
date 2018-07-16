package com.imooc.util;

import com.imooc.vo.ResultVO;

/**
 * @author: icych
 * @description:
 * @date: Created on 16:54 2018/7/15
 */
@SuppressWarnings("unchecked")
public class ResultVOUtil {

    private ResultVOUtil() {
    }

    public static ResultVO success(Object data) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(data);
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
