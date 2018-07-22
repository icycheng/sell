<#assign ctx=request.contextPath/>
<html>
<#include "../common/header.ftl">
<body>
<div id="wrapper" class="toggled">
    <div id="page-content-wrapper">
    <#-- 侧边栏sidebar -->
        <#include "../common/nav.ftl">
    <#-- 主要内容Content -->
        <div class="container-fluid">
            <div class="row clearfix">
                <#-- 订单总金额及支付状态 -->
                <div class="col-md-6 column">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>订单编号</th>
                            <th>订单总额</th>
                            <th>订单状态</th>
                            <th>支付状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>${orderDTO.orderId}</td>
                            <td>${orderDTO.orderAmount}</td>
                            <td>${orderDTO.getOrderStatusEnum().desc}</td>
                            <td>${orderDTO.getPayStatusEnum().desc}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <#-- 订单详情 -->
                <div class="col-md-9 column">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>商品id</th>
                            <th>商品名称</th>
                            <th>价格</th>
                            <th>数量</th>
                            <th>总额</th>
                        </tr>
                        </thead>
                        <tbody>
                    <#list orderDTO.orderDetailList as orderDetail>
                    <tr>
                        <td>${orderDetail.productId}</td>
                        <td>${orderDetail.productName}</td>
                        <td>${orderDetail.productPrice}</td>
                        <td>${orderDetail.productQuantity}</td>
                        <td>${orderDetail.productQuantity * orderDetail.productPrice}</td>
                    </tr>
                    </#list>
                        </tbody>
                    </table>
                </div>

                <#-- 按钮组 -->
                <div class="col-md-12 column">
                <#if orderDTO.getOrderStatusEnum().desc == '新订单'>
                    <a href="/sell/seller/order/finish?orderId=${orderDTO.orderId}" type="button"
                       class="btn btn-default btn-primary">完结订单</a>
                    <a href="/sell/seller/order/cancel?orderId=${orderDTO.orderId}" type="button"
                       class="btn btn-default btn-danger">取消订单</a>
                </#if>
                    <button id="goback-btn" type="button" class="btn btn-default btn-success">返回
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.bootcss.com/jquery/2.1.0/jquery.js"></script>
<script>
    $(function () {
        $('#goback-btn').click(function () {
            history.go(-1);
        });
    });
</script>
</body>
</html>