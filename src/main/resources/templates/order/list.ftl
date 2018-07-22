<#assign ctx=request.contextPath/>
<html>
<#include "../common/header.ftl">
<body>
<div id="wrapper" class="toggled">
<#-- 侧边栏sidebar -->
    <#include "../common/nav.ftl">
<#-- 主要内容Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-striped table-hover">
                        <thead>
                        <tr>
                            <th>订单编号</th>
                            <th>姓名</th>
                            <th>手机号</th>
                            <th>地址</th>
                            <th>金额</th>
                            <th>订单状态</th>
                            <th>支付状态</th>
                            <th>创建时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>

                        <tbody>
                            <#list orderDTOPage.content as orderDTO>
                            <tr>
                                <td>${orderDTO.orderId}</td>
                                <td>${orderDTO.buyerName}</td>
                                <td>${orderDTO.buyerPhone}</td>
                                <td>${orderDTO.buyerAddress}</td>
                                <td>${orderDTO.orderAmount}</td>
                                <td>${orderDTO.getOrderStatusEnum().desc}</td>
                                <td>${orderDTO.getPayStatusEnum().desc}</td>
                                <td>${orderDTO.createTime}</td>
                                <td>
                                    <a href="${ctx}/seller/order/detail?orderId=${orderDTO.orderId}">详情</a>
                                </td>
                                <td>
                                <#if orderDTO.getOrderStatusEnum().desc == '新订单'>
                                    <a href="${ctx}/seller/order/cancel?orderId=${orderDTO.orderId}">取消</a>
                                </#if>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>

                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                        <#if currentPage lte 1>
                            <li class="disabled">
                                <a href="javascript:void(0)">上一页</a>
                            </li>
                        <#else>
                            <li>
                                <a href="${ctx}/seller/order/list?page=${currentPage - 1}&size=${size}">上一页</a>
                            </li>
                        </#if>

                        <#list 1..orderDTOPage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="javascript:void(0)">${index}</a></li>
                            <#else>
                                <li>
                                    <a href="${ctx}/seller/order/list?page=${index}&size=${size}">${index}</a>
                                </li>
                            </#if>
                        </#list>

                        <#if currentPage gte orderDTOPage.getTotalPages()>
                            <li class="disabled">
                                <a href="javascript:void(0)">下一页</a>
                            </li>
                        <#else>
                            <li>
                                <a href="${ctx}/seller/order/list?page=${currentPage + 1}&size=${size}">下一页</a>
                            </li>
                        </#if>
                        <span style="line-height: 34px; font-size: 16px; margin-left: 15px;">
                            当前第<strong>${currentPage}</strong>页
                            共<strong>${orderDTOPage.getTotalElements()}</strong>条数据&nbsp;&nbsp;
                        </span>
                    </ul>
                </div>
            </div>
        </div>
    </div>

</div>

<#-- 消息提示模态框 -->
<div class="modal fade" id="msg-modal" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    消息
                </h4>
            </div>
            <div class="modal-body">
                你有新的订单
            </div>
            <div class="modal-footer">
                <button type="button" onclick="$('#notice').get(0).pause()" class="btn btn-default btn-danger" data-dismiss="modal">关闭
                </button>
                <button type="button" onclick="location.reload()" class="btn btn-primary">查看新订单</button>
            </div>
        </div>
    </div>
</div>

<#-- 播放音乐 -->
<audio id="notice" loop="loop">
    <source src="${ctx}/mp3/song.mp3" type="audio/mpeg">
</audio>

<script src="https://cdn.bootcss.com/jquery/2.1.0/jquery.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script>
    var websocket = null;
    if ('WebSocket' in window) {
        websocket = new WebSocket('ws://icyc.natapp1.cc/sell/webSocket');
    } else {
        alert('该浏览器不支持websocket!');
    }

    websocket.onopen = function (ev) {
        console.log('建立连接');
    };

    websocket.onclose = function (ev) {
        console.log('连接关闭');
    };

    websocket.onmessage = function (ev) {
        console.log('收到消息: ' + ev.data);
        //弹窗提醒,播放音乐
        $('#msg-modal').modal('show');
        $('#notice').get(0).play();
    };

    websocket.onerror = function (ev) {
        alert('websocket通信发生错误!');
    };

    window.onbeforeunload = function () {
        websocket.close();
    }

</script>
</body>
</html>